import { useState } from 'react';
import { plateAPI } from '../services/api';

const PlateUpload = ({ onDetected }) => {
  const [file, setFile] = useState(null);
  const [result, setResult] = useState('');
  const [loading, setLoading] = useState(false);
  const [preview, setPreview] = useState(null);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    setFile(selectedFile);
    
    // Tạo preview ảnh
    if (selectedFile) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreview(reader.result);
      };
      reader.readAsDataURL(selectedFile);
    } else {
      setPreview(null);
    }
  };

  const handleUpload = async () => {
    if (!file) {
      alert('Vui lòng chọn ảnh biển số');
      return;
    }

    try {
      setLoading(true);
      const res = await plateAPI.uploadPlateImage(file);
      
      // Lấy kết quả từ response (field 'plate' theo backend)
      const detectedPlate = res.data.plate || res.data.plateNumber;
      
      setResult(detectedPlate || 'Không nhận diện được');
      
      // Gọi callback để điền vào ô input
      if (detectedPlate && onDetected) {
        onDetected(detectedPlate);
      }
      
    } catch (err) {
      console.error(err);
      alert('Upload thất bại');
      setResult('');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-blue-50 border-2 border-blue-200 rounded-xl p-4 space-y-3">
      <div className="flex items-center gap-2 mb-2">
        <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
        </svg>
        <h3 className="text-sm font-bold text-blue-800 uppercase">Nhận diện biển số từ ảnh</h3>
      </div>

      {/* Preview ảnh */}
      {preview && (
        <div className="relative w-full h-40 bg-slate-100 rounded-lg overflow-hidden border-2 border-slate-300">
          <img src={preview} alt="Preview" className="w-full h-full object-contain" />
        </div>
      )}

      {/* Input file */}
      <label className="block">
        <input
          type="file"
          accept="image/*"
          onChange={handleFileChange}
          className="block w-full text-sm text-slate-600 file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:text-sm file:font-semibold file:bg-blue-100 file:text-blue-700 hover:file:bg-blue-200 file:cursor-pointer cursor-pointer"
        />
      </label>

      {/* Button Upload */}
      <button 
        onClick={handleUpload} 
        disabled={loading || !file}
        className="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
      >
        {loading ? (
          <>
            <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
            <span>Đang xử lý...</span>
          </>
        ) : (
          <>
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
            </svg>
            <span>Nhận diện biển số</span>
          </>
        )}
      </button>

      {/* Kết quả */}
      {result && (
        <div className={`p-3 rounded-lg border-2 ${result === 'Không nhận diện được' ? 'bg-red-50 border-red-300' : 'bg-green-50 border-green-300'}`}>
          <p className="text-xs font-semibold text-slate-600 mb-1">KẾT QUẢ NHẬN DIỆN:</p>
          <p className={`text-2xl font-black uppercase tracking-wider ${result === 'Không nhận diện được' ? 'text-red-600' : 'text-green-700'}`}>
            {result}
          </p>
        </div>
      )}
    </div>
  );
};

export default PlateUpload;
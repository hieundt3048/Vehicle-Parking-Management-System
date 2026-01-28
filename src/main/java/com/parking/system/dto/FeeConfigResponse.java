package com.parking.system.dto;

/**
 * DTO for Fee Configuration Response
 * Chỉ chứa các thông tin cần thiết cho Frontend
 */
public class FeeConfigResponse {
    private double motorbikeFirstBlock;
    private double motorbikeNextHour;
    private double carFirstBlock;
    private double carNextHour;
    private long firstBlockHours;
    
    public FeeConfigResponse() {
    }
    
    public FeeConfigResponse(double motorbikeFirstBlock, double motorbikeNextHour, 
                            double carFirstBlock, double carNextHour, long firstBlockHours) {
        this.motorbikeFirstBlock = motorbikeFirstBlock;
        this.motorbikeNextHour = motorbikeNextHour;
        this.carFirstBlock = carFirstBlock;
        this.carNextHour = carNextHour;
        this.firstBlockHours = firstBlockHours;
    }
    
    public double getMotorbikeFirstBlock() {
        return motorbikeFirstBlock;
    }
    
    public void setMotorbikeFirstBlock(double motorbikeFirstBlock) {
        this.motorbikeFirstBlock = motorbikeFirstBlock;
    }
    
    public double getMotorbikeNextHour() {
        return motorbikeNextHour;
    }
    
    public void setMotorbikeNextHour(double motorbikeNextHour) {
        this.motorbikeNextHour = motorbikeNextHour;
    }
    
    public double getCarFirstBlock() {
        return carFirstBlock;
    }
    
    public void setCarFirstBlock(double carFirstBlock) {
        this.carFirstBlock = carFirstBlock;
    }
    
    public double getCarNextHour() {
        return carNextHour;
    }
    
    public void setCarNextHour(double carNextHour) {
        this.carNextHour = carNextHour;
    }
    
    public long getFirstBlockHours() {
        return firstBlockHours;
    }
    
    public void setFirstBlockHours(long firstBlockHours) {
        this.firstBlockHours = firstBlockHours;
    }
}

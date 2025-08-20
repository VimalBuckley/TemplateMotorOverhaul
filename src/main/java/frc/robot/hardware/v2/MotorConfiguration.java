package frc.robot.hardware.v2;


import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;

public class MotorConfiguration implements Cloneable {
    public Current statorCurrentLimit = Amps.of(120);
    public boolean statorCurrentLimitEnable = true;
    public Current supplyCurrentLimit = Amps.of(70);
    public boolean supplyCurrentLimitEnable = true;
    public Voltage maxNegativeVoltage = Volts.of(-12);
    public Voltage maxPositiveVoltage = Volts.of(12);
    public boolean inverted = false;
    public boolean brakeModeEnabled = false;

    public MotorConfiguration withStatorCurrentLimit(Current limit) {
        statorCurrentLimit = limit;
        return this;
    }

    public MotorConfiguration withStatorCurrentLimitEnable(boolean enable) {
        statorCurrentLimitEnable = enable;
        return this;
    }

    public MotorConfiguration withSupplyCurrentLimit(Current limit) {
        supplyCurrentLimit = limit;
        return this;
    }

    public MotorConfiguration withSupplyCurrentLimitEnable(boolean enable) {
        supplyCurrentLimitEnable = enable;
        return this;
    }

    public MotorConfiguration withMaxNegativeVoltage(Voltage volts) {
        maxNegativeVoltage = volts;
        return this;
    }

    public MotorConfiguration withMaxPositiveVoltage(Voltage volts) {
        maxPositiveVoltage = volts;
        return this;
    }

    public MotorConfiguration withInverted(boolean isInverted) {
        inverted = isInverted;
        return this;
    }

    public MotorConfiguration withBrakeModeEnabled(boolean enabled) {
        brakeModeEnabled = enabled;
        return this;
    }

    @Override
    public MotorConfiguration clone() {
        MotorConfiguration config = new MotorConfiguration();
        config.statorCurrentLimit = statorCurrentLimit;
        config.statorCurrentLimitEnable = statorCurrentLimitEnable;
        config.supplyCurrentLimit = supplyCurrentLimit;
        config.supplyCurrentLimitEnable = supplyCurrentLimitEnable;
        config.maxNegativeVoltage = maxNegativeVoltage;
        config.maxPositiveVoltage = maxPositiveVoltage;
        config.inverted = inverted;
        config.brakeModeEnabled = brakeModeEnabled;
        return config;
    }
}

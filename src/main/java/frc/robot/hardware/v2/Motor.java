package frc.robot.hardware.v2;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.util.function.BooleanConsumer;

public class Motor {
    private HashMap<Motor, Boolean> followers;
    private Consumer<Voltage> voltageConsumer;
    private Consumer<Angle> positionConsumer;
    private BooleanConsumer brakeModeConsumer;
    private Supplier<Angle> positionSupplier;
    private Supplier<AngularVelocity> velocitySupplier;
    private MotorConfiguration config;
    private DCMotor model;

    public void setVoltage(Voltage volts) {
        for (Motor motor : followers.keySet()) {
            motor.setVoltage(volts);
        }
        if (config.inverted) {
            volts = volts.unaryMinus();
        }
        if (config.statorCurrentLimitEnable) {
            double IR = config.statorCurrentLimit.in(Amps) * model.rOhms;
            double kVw = (1 / model.KvRadPerSecPerVolt) * velocitySupplier.get().in(RadiansPerSecond);
            volts = Volts.of(MathUtil.clamp(
                volts.in(Volts), 
                kVw - IR, 
                kVw + IR
            ));
        }
        if (config.supplyCurrentLimitEnable) {
            double statorCurrent = Math.abs((volts.in(Volts) - (1 / model.KvRadPerSecPerVolt) * velocitySupplier.get().in(RadiansPerSecond)) / model.rOhms);
            volts = Volts.of(MathUtil.clamp(
                volts.in(Volts), 
                -12 * config.supplyCurrentLimit.in(Amps) / statorCurrent, 
                12 * config.supplyCurrentLimit.in(Amps) / statorCurrent)
            );
        }
        volts = Volts.of(MathUtil.clamp(volts.in(Volts), config.maxNegativeVoltage.in(Volts), config.maxPositiveVoltage.in(Volts)));
        voltageConsumer.accept(volts);
    }

    public Angle getPosition() {
        if (config.inverted) {
            return positionSupplier.get().unaryMinus();
        }
        return positionSupplier.get();
    }
    
    public AngularVelocity getVelocity() {
        if (config.inverted) {
            return velocitySupplier.get().unaryMinus();
        }
        return velocitySupplier.get();
    }

    public void addMotor(Motor motor, boolean useParentConfig) {
        followers.put(motor, useParentConfig);
        if (useParentConfig) {
            motor.applyConfig(config);
        }
    }

    public void resetPosition(Angle correctPosition) {
        positionConsumer.accept(correctPosition);
    }

    public void overridePositionEncoder(Supplier<Angle> positionSupplier) {
        this.positionSupplier = positionSupplier;
    }

    public void overrideVelocityEncoder(Supplier<AngularVelocity> velocitySupplier) {
        this.velocitySupplier = velocitySupplier;
    }

    public MotorConfiguration getConfig() {
        return config.clone();
    }

    public void applyConfig(MotorConfiguration config) {
        brakeModeConsumer.accept(config.brakeModeEnabled);
        this.config = config;
        for (Motor follower : followers.keySet()) {
            if (followers.get(follower)) {
                follower.applyConfig(config);
            }
        }
    }
}

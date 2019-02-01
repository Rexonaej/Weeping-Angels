package me.sub.angels.client.models.poses;

import me.sub.angels.common.entities.EntityWeepingAngel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Cuboid;

public abstract class PoseBase {

    private float limbSwing, limbSwingAmount, ageInTicks, netheadYaw, headPitch, swingProgress;
	private EntityWeepingAngel angel;
	
	public PoseBase() {}

    public PoseBase(EntityWeepingAngel angel, float limbSwing, float limbSwingAmount, float ageInTicks, float netheadYaw, float headPitch, float swingProgress) {
		this.limbSwing = limbSwing;
		this.limbSwingAmount = limbSwingAmount;
		this.ageInTicks = ageInTicks;
		this.netheadYaw = netheadYaw;
		this.headPitch = headPitch;
		this.swingProgress = swingProgress;
		this.angel = angel;
	}
	
	/**
	 * Used to set the Models arm angles
	 */
	public abstract void setArmAngles(Cuboid left_arm, Cuboid right_arm, Cuboid wrist_left, Cuboid wrist_right);
	
	/**
	 * Used to set the Models head angles
	 */
	public abstract void setHeadAngles(Cuboid head);
	
	/**
	 * Determines angry face
	 */
	public abstract boolean angryFace();

    /**
	 * Determines angry face
	 */
	public abstract void setBodyAngles(Cuboid body);
	
	/**
	 * Basically I never use this, it's there for the sake of it, used to set wing angles
	 */
	public abstract void setWingAngles(Cuboid left_wing, Cuboid right_wing);
	
	/**
	 * Returns the entities Limb Swing
	 */
	public float getLimbSwing() {
		return limbSwing;
	}
	
	/**
	 * Returns the entities Limb Swing amount
	 */
	public float getLimbSwingAmount() {
		return limbSwingAmount;
	}
	
	/**
	 * Returns the entities age in ticks
	 */
	public float getAgeInTicks() {
		return ageInTicks;
	}
	
	/**
	 * Returns the entities head pitch
	 */
	public float getHeadPitch() {
		return headPitch;
	}
	
	/**
	 * Returns the entities head yaw
	 */
	public float getNetheadYaw() {
		return netheadYaw;
	}
	
	/**
	 * Returns the entities Swing progress
	 */
	public float getSwingProgress() {
		return swingProgress;
	}
	
	/**
	 * Returns the entities in use
	 */
	public EntityWeepingAngel getAngel() {
		return angel;
	}
	
	/**
	 * Converts degrees to radians, because fuck radians
	 */
	public float degreeToRadian(float degree) {
		return (float) (degree * Math.PI / 180);
	}
	
    @Environment(EnvType.CLIENT)
	public void resetAngles(Cuboid model) {
		model.pitch = 0;
		model.yaw = 0;
		model.roll = 0;
	}
	
}

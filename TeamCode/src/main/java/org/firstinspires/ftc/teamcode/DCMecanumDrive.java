package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.AthenaMecanumDrive;
import org.firstinspires.ftc.teamcode.util.PoseStorage;

@TeleOp(name="DC Mecanum Drive", group="driver centric")
public class DCMecanumDrive extends LinearOpMode {
    @Override
    public void runOpMode() {
        ElapsedTime runtime = new ElapsedTime();

        // Initialize the hardware
        AthenaMecanumDrive drive = new AthenaMecanumDrive(hardwareMap);

        // Turn off velocity control for teleop
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set your initial pose to x: 10, y: 10, facing 90 degrees
        drive.setPoseEstimate(PoseStorage.currentPose);

        // Add ready status to telemetry
        telemetry.addData("Status", "<-- Press play to begin");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        runtime.reset();
        while (opModeIsActive() && !isStopRequested()) {
            Pose2d poseEstimate = drive.getPoseEstimate();

            Vector2d input = new Vector2d(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x
            ).rotated(-poseEstimate.getHeading());

            drive.setWeightedDrivePower(
                    new Pose2d(
                            input.getX(),
                            input.getY(),
                            -gamepad1.right_stick_x
                    )
            );

            // Update drive parameters
            drive.update();

            // Print pose to telemetry
            telemetry.addData("Runtime", runtime.toString());
            telemetry.addData("X", poseEstimate.getX());
            telemetry.addData("Y", poseEstimate.getY());
            telemetry.addData("Heading", poseEstimate.getHeading());
            telemetry.update();
        }
    }
}
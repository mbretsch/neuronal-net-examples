package net.mbretsch.neuronalnet.example.robot;

/**
 * Beautifier for the Robot example, that creates readable output.
 * @author mbretsch
 *
 */
public class RobotReporter {

	public enum RobotMove {
		NORTH("north"), EAST("east"), SOUTH("south"), WEST("west");

		private final String text;

		RobotMove(String text) {
			this.text = text;
		}

		public static RobotMove getRobotMove(double[] output) {
			if (output[0] > output[1] && output[0] > output[2] && output[0] > output[3]) {
				return RobotMove.NORTH;
			} else if (output[1] > output[0] && output[1] > output[2] && output[1] > output[3]) {
				return RobotMove.EAST;
			}
			if (output[2] > output[0] && output[2] > output[1] && output[2] > output[3]) {
				return RobotMove.SOUTH;
			} else {
				return RobotMove.WEST;
			}
		}

		public String toString() {
			return this.text;
		}
	}

	public void printReport(double[] input, double[] output) {
		System.out.println("#####");
		System.out.format("#%.0f%.0f%.0f#\n", input[0], input[1], input[2]);
		System.out.format("#%.0fR%.0f#\n", input[7], input[3]);
		System.out.format("#%.0f%.0f%.0f#\n", input[6], input[5], input[4]);
		System.out.println("#####");
		System.out.println("Robot moves " + RobotMove.getRobotMove(output));
	}

	public void printReports(double[][] inputs, double[][] outputs) {
		for (int i = 0; i < inputs.length; i++) {
			System.out.println("Report " + (i + 1) + ":");
			System.out.println("--------");
			printReport(inputs[i], outputs[i]);
			System.out.println();
		}
	}
}

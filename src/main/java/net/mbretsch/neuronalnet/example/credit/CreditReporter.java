package net.mbretsch.neuronalnet.example.credit;

/**
 * Simple beautifier for the Credit Example.
 * @author mbretsch
 *
 */
public class CreditReporter {

	public enum CreditHistory {

		BAD("schlecht"), UNKNOWN("unbekannt"), GOOD("gut");

		private final String text;

		CreditHistory(String text) {
			this.text = text;
		}

		public static CreditHistory getCreditHistory(double[] i) {
			if (i[0] == 1d) {
				return BAD;
			} else if (i[1] == 1d) {
				return UNKNOWN;
			} else {
				return GOOD;
			}
		}

		public String toString() {
			return "Kreditvergangenheit: " + this.text;
		}
	}

	public enum Debts {
		HIGH("hoch"), LOW("niedrig");

		private final String text;

		Debts(String text) {
			this.text = text;
		}

		public static Debts getDebt(double[] i) {
			if (i[3] == 1d) {
				return HIGH;
			} else {
				return LOW;
			}
		}

		public String toString() {
			return "Schuldenh�he: " + this.text;
		}
	}

	public enum Collaterals {
		NOTEXISTANT("nicht vorhanden"), EXISTANT("vorhanden");

		private final String text;

		Collaterals(String text) {
			this.text = text;
		}

		public static Collaterals getCollaterals(double[] i) {
			if (i[5] == 1d) {
				return NOTEXISTANT;
			} else {
				return EXISTANT;
			}
		}

		public String toString() {
			return "Sicherheiten: " + this.text;
		}
	}

	public enum Income {
		LOW("niedrig"), NORMAL("normal"), HIGH("hoch");

		private final String text;

		Income(String text) {
			this.text = text;
		}

		public static Income getIncome(double[] i) {
			if (i[7] == 1d) {
				return LOW;
			} else if (i[8] == 1d) {
				return NORMAL;
			} else {
				return HIGH;
			}
		}

		public String toString() {
			return "Einkommen: " + this.text;
		}
	}

	public enum Credibility {
		BAD("schlecht"), NORMAL("normal"), GOOD("gut");

		private final String text;

		Credibility(String text) {
			this.text = text;
		}

		public static Credibility getCredibility(double[] o) {
			if (o[0] > o[1] && o[0] > o[2]) {
				return BAD;
			} else if (o[1] > o[0] && o[1] > o[2]) {
				return NORMAL;
			} else {
				return GOOD;
			}
		}

		public String toString() {
			return "Kreditw�rdigkeit: " + this.text;
		}
	}

	public void printReport(double[] input, double[] output) {
		CreditHistory creditHistory = CreditHistory.getCreditHistory(input);
		Debts debts = Debts.getDebt(input);
		Collaterals collaterals = Collaterals.getCollaterals(input);
		Income income = Income.getIncome(input);
		Credibility credibility = Credibility.getCredibility(output);

		System.out.println(creditHistory);
		System.out.println(debts);
		System.out.println(collaterals);
		System.out.println(income);
		System.out.print(credibility + " (");
		System.out.format("%.2f; ", output[0]);
		System.out.format("%.2f; ", output[1]);
		System.out.format("%.2f)\n", output[2]);

	}

	public void printReports(double[][] inputs, double[][] outputs) {
		for (int i = 0; i < inputs.length; i++) {
			System.out.println("Report " + (i + 1));
			System.out.println("---------");
			printReport(inputs[i], outputs[i]);
			System.out.println();
		}
	}

}

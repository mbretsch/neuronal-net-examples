package net.mbretsch.neuronalnet.example.credit;

import net.mbretsch.neuronalnet.config.Config;
import net.mbretsch.neuronalnet.data.DatasetLoader;
import net.mbretsch.neuronalnet.data.TrainingPatternLoader;
import net.mbretsch.neuronalnet.kohonen.Kohonen;

/**
 * Example that uses Self Organizing Maps for partioning of creditworthiness.
 * @author mbretsch
 *
 */
public class KohonenCredit {

	public static void main(String[] args) {
		Config.loadProperties();

		TrainingPatternLoader tpl = new TrainingPatternLoader();
		tpl.loadPattern("creditTraining.pat");

		DatasetLoader dsl = new DatasetLoader();
		dsl.loadPattern("creditData.pat");

		Kohonen kohonen = new Kohonen(tpl.getInputs()[0].length, 50);
		kohonen.setEuclidean(true);

		kohonen.trainMap(tpl.getInputs(), 500);

		for (int i = 0; i < dsl.getInputs().length; i++) {
			kohonen.setInputs(dsl.getInputs()[i]);
			kohonen.step();
		}

		kohonen.plot();
	}

}

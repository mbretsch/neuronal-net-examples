package net.mbretsch.neuronalnet.example.credit;

import java.util.ArrayList;
import java.util.List;

import net.mbretsch.neuronalnet.Layer;
import net.mbretsch.neuronalnet.NeuralNetwork;
import net.mbretsch.neuronalnet.Neuron;
import net.mbretsch.neuronalnet.activators.ActivationStrategy;
import net.mbretsch.neuronalnet.activators.SigmoidActivationStrategy;
import net.mbretsch.neuronalnet.backprop.Backprop;
import net.mbretsch.neuronalnet.backprop.BackpropAlgorithm;
import net.mbretsch.neuronalnet.backprop.SimpleBackprop;
import net.mbretsch.neuronalnet.config.Config;
import net.mbretsch.neuronalnet.data.DatasetLoader;
import net.mbretsch.neuronalnet.data.TrainingPatternLoader;
import net.mbretsch.neuronalnet.modification.ConnectionRemoval;
import net.mbretsch.neuronalnet.modification.NetModification;

/**
 * This example evaluates creditworthiness of a potential customer. It uses data like credit past, income or financial assets. 
 * Training data can be found in 'resources/creditTraining.pat', dataset for evaluation can be found in 'resource/creditData.pat'.
 * @author mbretsch
 *
 */
public class Credit {

	public static void main(String[] args) {

		Config.loadProperties();

		TrainingPatternLoader patternLoader = new TrainingPatternLoader();

		patternLoader.loadPattern("creditTraining.pat");

		DatasetLoader datasetLoader = new DatasetLoader();

		datasetLoader.loadPattern("creditData.pat");

		ActivationStrategy as = new SigmoidActivationStrategy(2.0d);

		Layer iLayer = new Layer();
		for (int i = 0; i < patternLoader.getInputs()[0].length; i++) {
			iLayer.addNeuron(new Neuron(as));
		}

		Layer hLayer = new Layer(iLayer);
		for (int i = 0; i < 8; i++) {
			hLayer.addNeuron(new Neuron(as));
		}

		Layer oLayer = new Layer(hLayer);
		for (int i = 0; i < patternLoader.getOutputs()[0].length; i++) {
			oLayer.addNeuron(new Neuron(as));
		}

		NeuralNetwork creditNetwork = new NeuralNetwork("Credit Network");
		creditNetwork.addLayer(iLayer);
		creditNetwork.addLayer(hLayer);
		creditNetwork.addLayer(oLayer);
		List<NetModification> modList = new ArrayList<NetModification>();
		modList.add(new ConnectionRemoval(creditNetwork));
		BackpropAlgorithm bpAlg = new SimpleBackprop(creditNetwork, 0.6d);
		Backprop bProp = new Backprop(creditNetwork, bpAlg, modList);
		double error = bProp.batchLearning(patternLoader.getInputs(), patternLoader.getOutputs(), 2000);
		System.out.format("Error of last backprop Run: %.2f\n", error);

		double[][] inputs = datasetLoader.getInputs();
		double[][] outputs = new double[inputs.length][];

		for (int i = 0; i < inputs.length; i++) {
			creditNetwork.setInputs(inputs[i]);
			outputs[i] = creditNetwork.getOutput();
		}

		CreditReporter cr = new CreditReporter();
		cr.printReports(inputs, outputs);

		bProp.plot();

	}

}

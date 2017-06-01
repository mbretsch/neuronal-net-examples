package net.mbretsch.neuronalnet.example.letter;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.mbretsch.neuronalnet.Layer;
import net.mbretsch.neuronalnet.NeuralNetwork;
import net.mbretsch.neuronalnet.Neuron;
import net.mbretsch.neuronalnet.activators.ActivationStrategy;
import net.mbretsch.neuronalnet.activators.SigmoidActivationStrategy;
import net.mbretsch.neuronalnet.backprop.Backprop;
import net.mbretsch.neuronalnet.backprop.BackpropAlgorithm;
import net.mbretsch.neuronalnet.backprop.BackpropWithMomentum;
import net.mbretsch.neuronalnet.backprop.RProp;
import net.mbretsch.neuronalnet.config.Config;
import net.mbretsch.neuronalnet.data.PatternFromPng;
import net.mbretsch.neuronalnet.data.TrainingPatternLoader;
import net.mbretsch.neuronalnet.modification.ConnectionRemoval;
import net.mbretsch.neuronalnet.modification.NetModification;

/**
 * Example for recognizing letters. A 5x7 image is mapped to 26 outputs to classify letters. Training-Data in 'resources/letters/5x7/uppercase', Test-Data with distored letters in 'resources/test/'.
 * @author mbretsch
 *
 */
public class Letter {

	public static void main(String[] args) {

		Config.loadProperties();

		TrainingPatternLoader tpl = new TrainingPatternLoader();
		tpl.loadPatternsFromPNG("letters/5x7/uppercase");

		NeuralNetwork letterNetwork = createNetwork(tpl.getInputs()[0].length, tpl.getOutputs()[0].length);
		NeuralNetwork letterNetwork2 = createNetwork(tpl.getInputs()[0].length, tpl.getOutputs()[0].length);

		List<NetModification> modList = new ArrayList<NetModification>();
		modList.add(new ConnectionRemoval(letterNetwork));
		BackpropAlgorithm bpAlg = new BackpropWithMomentum(letterNetwork, 0.6d, 0.1d);
		Backprop backprop = new Backprop(letterNetwork, bpAlg, modList);

		List<NetModification> modList2 = new ArrayList<NetModification>();
		modList2.add(new ConnectionRemoval(letterNetwork2));
		BackpropAlgorithm bpmAlg = new RProp(letterNetwork2);
		Backprop backpropMomentum = new Backprop(letterNetwork2, bpmAlg, modList2);

		double error = 0;
		error = backprop.onlineLearning(tpl.getInputs(), tpl.getOutputs(), 600);
		System.out.println("Last learning error: " + error);
		backprop.printModifications();

		error = backpropMomentum.onlineLearning(tpl.getInputs(), tpl.getOutputs(), 600);
		System.out.println("Last learning error: " + error);
		backpropMomentum.printModifications();

		PatternFromPng pfp = new PatternFromPng();

		double[] input = null;
		try {
			input = pfp.getPatternFromPNG(new File(Letter.class.getClassLoader().getResource("test/x.png").toURI()));
			letterNetwork2.setInputs(input);
			double[] output = letterNetwork2.getOutput();

			System.out.println("Input: x");
			System.out.print("(");
			for (int i = 0; i < output.length; i++) {
				System.out.printf("%.2f", output[i]);
				if (i < output.length - 1) {
					System.out.print(";");
				}
			}
			System.out.println(")");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		backpropMomentum.plot();

	}

	private static NeuralNetwork createNetwork(int countInputs, int countOutputs) {
		ActivationStrategy as = new SigmoidActivationStrategy(2.0d);

		Layer iLayer = new Layer();
		for (int i = 0; i < countInputs; i++) {
			iLayer.addNeuron(new Neuron(as));
		}

		Layer hLayer = new Layer(iLayer);
		for (int i = 0; i < 8; i++) {
			hLayer.addNeuron(new Neuron(as));
		}

		Layer oLayer = new Layer(hLayer);
		for (int i = 0; i < countOutputs; i++) {
			oLayer.addNeuron(new Neuron(as));
		}

		NeuralNetwork letterNetwork = new NeuralNetwork("Letter Network");
		letterNetwork.addLayer(iLayer);
		letterNetwork.addLayer(hLayer);
		letterNetwork.addLayer(oLayer);
		return letterNetwork;
	}

}

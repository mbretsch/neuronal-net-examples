package net.mbretsch.neuronalnet.example.robot;

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

/**
 * This example trains a little robot to move through a labyrinth. The robot sees 8 surrounding tiles, but can only move in 4 cardinal directions. 
 * He tries to predict, which movement is optimal in relation to the training Data.
 * @author mbretsch
 *
 */
public class Robot {

	public static void main(String[] args) {

		Config.loadProperties();

		TrainingPatternLoader tpl = new TrainingPatternLoader();
		tpl.loadPattern("robotTraining.pat");

		DatasetLoader dl = new DatasetLoader();
		dl.loadPattern("robotData.pat");

		ActivationStrategy as = new SigmoidActivationStrategy(2d);

		Layer iLayer = new Layer();
		for (int i = 0; i < tpl.getInputs()[0].length; i++) {
			iLayer.addNeuron(new Neuron(as));
		}

		Layer hLayer = new Layer(iLayer);
		for (int i = 0; i < 5; i++) {
			hLayer.addNeuron(new Neuron(as));
		}

		Layer oLayer = new Layer(hLayer);
		for (int i = 0; i < tpl.getOutputs()[0].length; i++) {
			oLayer.addNeuron(new Neuron(as));
		}

		NeuralNetwork nn = new NeuralNetwork("Robot NN");
		nn.addLayer(iLayer);
		nn.addLayer(hLayer);
		nn.addLayer(oLayer);

		BackpropAlgorithm bpS = new SimpleBackprop(nn, 0.6d);

		Backprop backpropS = new Backprop(nn, bpS);

		backpropS.onlineLearning(tpl.getInputs(), tpl.getOutputs(), 50);

		backpropS.plot();

		RobotReporter rr = new RobotReporter();
		double[][] inputs = dl.getInputs();
		double[][] outputs = new double[inputs.length][];
		for (int i = 0; i < inputs.length; i++) {
			nn.setInputs(inputs[i]);
			outputs[i] = nn.getOutput();
		}
		rr.printReports(inputs, outputs);

	}

}

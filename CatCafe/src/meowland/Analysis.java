package meowland;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;

import java.io.IOException;

public class Analysis
{
    public void analys()
    {
        try
        {
            String simpleMlp = new ClassPathResource("simple_mlp.h5").getFile().getPath();
            MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);
            INDArray input = Nd4j.create(256, 100);
            INDArray output = model.output(input);
            model.fit(input, output);

        } catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e)
        {
            e.printStackTrace();
        }
    }
}

package sandbox;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.tantaman.eats.aop.priv.common.config.JSONConfig;

public class GSonTest {
	public static void main(String[] args) throws FileNotFoundException {
		Gson gson = new Gson();
		
		JSONConfig cfg = gson.fromJson(new FileReader("src/com/tantaman/eats/pub/config.json"), JSONConfig.class);
		
		
	}
}

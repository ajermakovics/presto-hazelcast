package plugin;

import java.util.Map;
import java.util.Map.Entry;

import com.facebook.presto.spi.Connector;
import com.facebook.presto.spi.ConnectorFactory;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastConnectorFactory implements ConnectorFactory {

	private Map<String, String> optionalConfig;

	public HazelcastConnectorFactory(Map<String, String> optionalConfig) {
		this.optionalConfig = optionalConfig;

		for(Entry e : optionalConfig.entrySet())
			System.out.println(e.getKey() + " = " + e.getValue());
	}

	@Override
	public String getName() {
		return "hazelcast";
	}

	@Override
	public Connector create(String connectorId, Map<String, String> requiredConfig) {

		//TODO: name from config
		HazelcastInstance hz = Hazelcast.getOrCreateHazelcastInstance(new Config("local"));

		HazelcastConnector con = new HazelcastConnector(connectorId, requiredConfig, hz);

		return con;
	}

}

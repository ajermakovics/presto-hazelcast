package plugin;

import java.util.Map;

import com.facebook.presto.spi.*;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastConnector implements Connector {

	private HazelcastInstance hz;
	private Map<String, String> config;
	private String connectorId;

	public HazelcastConnector(String connectorId, Map<String, String> config, HazelcastInstance hz) {
		this.hz = hz;
		this.config = config;
		this.connectorId = connectorId;
	}

	@Override
	public ConnectorHandleResolver getHandleResolver() {
		return new HazelcastHandleResolver(connectorId);
	}

	@Override
	public ConnectorMetadata getMetadata() {
		return new HazelcastConnectorMetadata(connectorId, hz);
	}

	@Override
	public ConnectorSplitManager getSplitManager() {
		return new HazelcastSplitManager(connectorId);
	}

	@Override
	public ConnectorRecordSetProvider getRecordSetProvider() {
		return new HazelcastRecordSetProvider(connectorId, hz);
	}

	@Override
	public ConnectorRecordSinkProvider getRecordSinkProvider() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ConnectorIndexResolver getIndexResolver() {
		throw new UnsupportedOperationException();
	}

}

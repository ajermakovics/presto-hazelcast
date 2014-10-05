package plugin;

import com.facebook.presto.spi.*;

public class HazelcastHandleResolver implements ConnectorHandleResolver {

	private String connectorId;

	public HazelcastHandleResolver(String connectorId) {
		this.connectorId = connectorId;
	}

	@Override
	public boolean canHandle(ConnectorTableHandle table) {
		return table instanceof HazelcastTableHandle && ((HazelcastTableHandle)table).getConnectorId().equals(connectorId)  ;
	}

	@Override
	public boolean canHandle(ConnectorColumnHandle col) {
		return col instanceof HazelcastColumnHandle && ((HazelcastColumnHandle)col).getConnectorId().equals(connectorId)  ;
	}

	@Override
	public boolean canHandle(ConnectorSplit split) {
		return split instanceof HazelcastSplit && ((HazelcastSplit)split).getConnectorId().equals(connectorId);
	}

	@Override
	public boolean canHandle(ConnectorIndexHandle indexHandle) {
		return false;
	}

	@Override
	public boolean canHandle(ConnectorOutputTableHandle tableHandle) {
		return false;
	}

	@Override
	public boolean canHandle(ConnectorInsertTableHandle tableHandle) {
		return false;
	}

	@Override
	public Class<? extends ConnectorTableHandle> getTableHandleClass() {
		return HazelcastTableHandle.class;
	}

	@Override
	public Class<? extends ConnectorColumnHandle> getColumnHandleClass() {
		return HazelcastColumnHandle.class;
	}

	@Override
	public Class<? extends ConnectorSplit> getSplitClass() {
		return HazelcastSplit.class;
	}

	@Override
	public Class<? extends ConnectorIndexHandle> getIndexHandleClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<? extends ConnectorOutputTableHandle> getOutputTableHandleClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<? extends ConnectorInsertTableHandle> getInsertTableHandleClass() {
		throw new UnsupportedOperationException();
	}

}

package plugin;

import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ConnectorHandleResolver;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.ConnectorTableHandle;

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
	public boolean canHandle(ColumnHandle col) {
		return col instanceof HazelcastColumnHandle && ((HazelcastColumnHandle)col).getConnectorId().equals(connectorId)  ;
	}

	@Override
	public boolean canHandle(ConnectorSplit split) {
		return split instanceof HazelcastSplit && ((HazelcastSplit)split).getConnectorId().equals(connectorId);
	}

	@Override
	public Class<? extends ConnectorTableHandle> getTableHandleClass() {
		return HazelcastTableHandle.class;
	}

	@Override
	public Class<? extends ColumnHandle> getColumnHandleClass() {
		return HazelcastColumnHandle.class;
	}

	@Override
	public Class<? extends ConnectorSplit> getSplitClass() {
		return HazelcastSplit.class;
	}

}

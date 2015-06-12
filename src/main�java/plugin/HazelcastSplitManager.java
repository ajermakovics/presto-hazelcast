package plugin;

import java.util.ArrayList;
import java.util.List;

import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ConnectorPartition;
import com.facebook.presto.spi.ConnectorPartitionResult;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.ConnectorSplitManager;
import com.facebook.presto.spi.ConnectorSplitSource;
import com.facebook.presto.spi.ConnectorTableHandle;
import com.facebook.presto.spi.FixedSplitSource;
import com.facebook.presto.spi.TupleDomain;
import com.facebook.presto.util.Types;

public class HazelcastSplitManager implements ConnectorSplitManager {

	private String connectorId;

	public HazelcastSplitManager(String connectorId) {
		this.connectorId = connectorId;
	}

	@Override
	public ConnectorPartitionResult getPartitions(ConnectorTableHandle table,
			TupleDomain<ColumnHandle> tupleDomain) {

		List<ConnectorPartition> partitions = new ArrayList<>();
		// pretending we have single partition for now
		partitions.add( new HazelcastPartition(table) );

		return new ConnectorPartitionResult(partitions , tupleDomain);
	}

	@Override
	public ConnectorSplitSource getPartitionSplits(ConnectorTableHandle table,
			List<ConnectorPartition> partitions) {

		List<ConnectorSplit> splits = new ArrayList<>();
		HazelcastTableHandle hzTable = Types.checkType(table, HazelcastTableHandle.class, "table");
		splits.add( new HazelcastSplit(connectorId, hzTable.getTable()) );

		return new FixedSplitSource(connectorId, splits);
	}

}

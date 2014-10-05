package plugin;

import static com.facebook.presto.util.Types.checkType;
import static plugin.HazelcastConnectorUtil.getColumns;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jersey.repackaged.com.google.common.collect.Iterables;

import com.facebook.presto.spi.*;
import com.google.common.collect.ImmutableList;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HazelcastConnectorMetadata extends ReadOnlyConnectorMetadata {

	private HazelcastInstance hz;
	private String connectorId;
	public static String SCHEMA_NAME = "maps";

	public HazelcastConnectorMetadata(String connectorId, HazelcastInstance hz) {
		this.hz = hz;
		this.connectorId = connectorId;
	}

	@Override
	public List<String> listSchemaNames(ConnectorSession session) {
		return ImmutableList.of(SCHEMA_NAME);
	}

	@Override
	public ConnectorTableHandle getTableHandle(ConnectorSession session,
			SchemaTableName tableName) {
		return new HazelcastTableHandle(connectorId, tableName);
	}

	@Override
	public ConnectorTableMetadata getTableMetadata(ConnectorTableHandle tableHandle) {

		HazelcastTableHandle table = checkType( tableHandle, HazelcastTableHandle.class, "tableHandle");

		return new HazelcastTableMetadata(hz, table);
	}

	@Override
	public List<SchemaTableName> listTables(ConnectorSession session,
			String schemaNameOrNull) {

		Iterable<IMap> imaps = Iterables.filter( hz.getDistributedObjects(), IMap.class );

		List<SchemaTableName> tables = new ArrayList<>();
		for (IMap iMap : imaps) {
			tables.add( new SchemaTableName(SCHEMA_NAME, iMap.getName()) );
		}

		return tables;
	}

	@Override
	public ConnectorColumnHandle getSampleWeightColumnHandle(ConnectorTableHandle tableHandle) {
		return null;
	}

	@Override
	public Map<String, ConnectorColumnHandle> getColumnHandles(ConnectorTableHandle tableHandle) {

		HazelcastTableHandle table = checkType(tableHandle, HazelcastTableHandle.class, "tableHandle");
		List<ColumnMetadata> cols = HazelcastConnectorUtil.getColumns(table.getTable(), hz);

		Map<String, ConnectorColumnHandle> colHandles = new LinkedHashMap<>();
		HazelcastColumnHandle col;
		for(ColumnMetadata md: cols) {
			col = new HazelcastColumnHandle(connectorId, md.getName(), md.getType(), md.getOrdinalPosition());
			colHandles.put(md.getName(), col);
		}

		return colHandles;
	}

	@Override
	public ColumnMetadata getColumnMetadata(ConnectorTableHandle tableHandle,
			ConnectorColumnHandle columnHandle) {
		return checkType(columnHandle, HazelcastColumnHandle.class, "tableHandle").getMetadata();
	}

	@Override
	public Map<SchemaTableName, List<ColumnMetadata>> listTableColumns(
			ConnectorSession session, SchemaTablePrefix prefix) {

		List<SchemaTableName> tables = listTables(session, SCHEMA_NAME);
		Map<SchemaTableName, List<ColumnMetadata>> columns = new LinkedHashMap<>();
		for (SchemaTableName table : tables) {
			columns.put(table, getColumns(table, hz) );
		}

		return columns;
	}

}

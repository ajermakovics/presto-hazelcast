package plugin;

import com.facebook.presto.spi.ConnectorTableHandle;
import com.facebook.presto.spi.SchemaTableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HazelcastTableHandle implements ConnectorTableHandle {

	private SchemaTableName tableName;
	private String connectorId;

	@JsonCreator
	public HazelcastTableHandle(
			 @JsonProperty("connectorId") String connectorId,
			 @JsonProperty("table") SchemaTableName tableName) {
		this.tableName = tableName;
		this.connectorId = connectorId;
	}

	@JsonProperty
	public SchemaTableName getTable() {
		return tableName;
	}

	@JsonProperty
	public String getConnectorId() {
		return connectorId;
	}
}

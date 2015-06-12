package plugin;

import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ColumnMetadata;
import com.facebook.presto.spi.type.Type;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HazelcastColumnHandle implements ColumnHandle {

	private String connectorId;
	private ColumnMetadata metadata;
	private String columnName;
	private Type columnType;
    private boolean partitionKey;
    private boolean hidden;

	@JsonCreator
	public HazelcastColumnHandle(
            @JsonProperty("connectorId") String connectorId,
            @JsonProperty("columnName") String columnName,
            @JsonProperty("columnType") Type columnType,
            @JsonProperty("partitionKey") boolean partitionKey,
            @JsonProperty("hidden") boolean hidden) {
		this.connectorId = connectorId;
		this.columnName = columnName;
		this.columnType = columnType;
		this.partitionKey = partitionKey;
		this.hidden = hidden;
		this.metadata = new ColumnMetadata(columnName, columnType, partitionKey, "comment", hidden);
	}

	@JsonProperty
	public String getConnectorId() {
		return connectorId;
	}

	@JsonProperty
	public String getColumnName() {
		return columnName;
	}

	@JsonProperty
	public boolean isPartitionKey() {
		return partitionKey;
	}

	@JsonProperty
	public boolean isHidden() {
        return hidden;
    }

	@JsonProperty
	public Type getColumnType() {
		return columnType;
	}

	@JsonIgnore
	public ColumnMetadata getMetadata() {
		return metadata;
	}


}

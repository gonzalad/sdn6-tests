package com.example.sdn6.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.exceptions.value.Uncoercible;
import org.neo4j.driver.types.Entity;
import org.neo4j.driver.types.IsoDuration;
import org.neo4j.driver.types.MapAccessor;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Point;
import org.neo4j.driver.types.Relationship;
import org.neo4j.driver.types.Type;
import org.neo4j.driver.types.TypeSystem;

/**
 * This adapter between Record and MapAccessor overrides
 * the root node from the originating record and replaces
 * it with another one.
 *
 * Usage similar to {@link RecordMapAccessor} but allows to
 * override the root node
 */
public class RootOverrideRecordMapAccessor implements MapAccessor {

	private final Record delegate;
	private final TypeSystem typeSystem;
	private final Value root;
	private final List<String> keys;
	private static final String ROOT_OVERRIDE_KEY = "__root_override";

	public RootOverrideRecordMapAccessor(Value root, Record delegate, TypeSystem typeSystem) {
		this.root = root;
		this.delegate = delegate;
		this.typeSystem = typeSystem;
		keys = new ArrayList<>();
		root.keys().forEach(keys::add);
		keys.add(ROOT_OVERRIDE_KEY);
	}

	@Override
	public Iterable<String> keys() {
		return this.keys;
	}

	@Override
	public boolean containsKey(String key) {
		return this.delegate.containsKey(key);
	}

	@Override
	public Value get(String key) {
		var value = this.delegate.get(key);
		if (ROOT_OVERRIDE_KEY.equals(key)) {
			return root;
		} else {
			return listValue(value);
		}
	}

	private Value listValue(Value value) {
		if (typeSystem.LIST().isTypeOf(value)) {
			return value;
		} else {
			return new ListWrapperValue(value, typeSystem);
		}

	}

	@Override
	public int size() {
		return this.delegate.size();
	}

	@Override
	public Iterable<Value> values() {
		return this.delegate.values();
	}

	@Override
	public <T> Iterable<T> values(Function<Value, T> mapFunction) {
		return this.delegate.values().stream().map(mapFunction).collect(Collectors.toList());
	}

	@Override
	public Map<String, Object> asMap() {
		return this.delegate.asMap();
	}

	@Override
	public <T> Map<String, T> asMap(Function<Value, T> mapFunction) {
		return this.delegate.asMap(mapFunction);
	}

	@Override
	public String toString() {
		return this.delegate.toString();
	}

	private static class ListWrapperValue implements Value {
		private final Value delegate;
		private final TypeSystem typeSystem;

		public ListWrapperValue(Value value, TypeSystem typeSystem) {
			this.delegate = value;
			this.typeSystem = typeSystem;
		}

		@Override
		public int size() {
			return 1;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public Iterable<String> keys() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public Value get(int index) {
			if (index == 0) {
				return delegate;
			} else {
				throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public Type type() {
			return typeSystem.LIST();
		}

		@Override
		public boolean hasType(Type type) {
			return typeSystem.LIST().equals(type);
		}

		@Override
		public boolean isTrue() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.BOOLEAN().name());
		}

		@Override
		public boolean isFalse() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.BOOLEAN().name());
		}

		@Override
		public boolean isNull() {
			return false;
		}

		@Override
		public Object asObject() {
			return List.of(delegate.asObject());
		}

		@Override
		public <T> T computeOrDefault(Function<Value, T> mapper, T defaultValue) {
			return mapper.apply( this );
		}

		@Override
		public boolean asBoolean() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.BOOLEAN().name());
		}

		@Override
		public boolean asBoolean(boolean defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.BOOLEAN().name());
		}

		@Override
		public byte[] asByteArray() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.BYTES().name());
		}

		@Override
		public byte[] asByteArray(byte[] defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.BYTES().name());
		}

		@Override
		public String asString() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.STRING().name());
		}

		@Override
		public String asString(String defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.STRING().name());
		}

		@Override
		public Number asNumber() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NUMBER().name());
		}

		@Override
		public long asLong() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NUMBER().name());
		}

		@Override
		public long asLong(long defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NUMBER().name());
		}

		@Override
		public int asInt() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NUMBER().name());
		}

		@Override
		public int asInt(int defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NUMBER().name());
		}

		@Override
		public double asDouble() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NUMBER().name());
		}

		@Override
		public double asDouble(double defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NUMBER().name());
		}

		@Override
		public float asFloat() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NUMBER().name());
		}

		@Override
		public float asFloat(float defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NUMBER().name());
		}

		@Override
		public List<Object> asList() {
			return List.of(delegate.asObject());
		}

		@Override
		public List<Object> asList(List<Object> defaultValue) {
			return List.of(delegate.asObject());
		}

		@Override
		public <T> List<T> asList(Function<Value, T> mapFunction) {
			return List.of(mapFunction.apply(delegate));
		}

		@Override
		public <T> List<T> asList(Function<Value, T> mapFunction, List<T> defaultValue) {
			return asList(mapFunction);
		}

		@Override
		public Entity asEntity() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NODE().name());
		}

		@Override
		public Node asNode() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.NODE().name());
		}

		@Override
		public Relationship asRelationship() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.RELATIONSHIP().name());
		}

		@Override
		public Path asPath() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.PATH().name());
		}

		@Override
		public LocalDate asLocalDate() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.LOCAL_DATE_TIME().name());
		}

		@Override
		public OffsetTime asOffsetTime() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.TIME().name());
		}

		@Override
		public LocalTime asLocalTime() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.LOCAL_TIME().name());
		}

		@Override
		public LocalDateTime asLocalDateTime() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.LOCAL_DATE_TIME().name());
		}

		@Override
		public OffsetDateTime asOffsetDateTime() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.LOCAL_DATE_TIME().name());
		}

		@Override
		public ZonedDateTime asZonedDateTime() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.DATE_TIME().name());
		}

		@Override
		public IsoDuration asIsoDuration() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.DURATION().name());
		}

		@Override
		public Point asPoint() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.POINT().name());
		}

		@Override
		public LocalDate asLocalDate(LocalDate defaultValue) {
			return asLocalDate();
		}

		@Override
		public OffsetTime asOffsetTime(OffsetTime defaultValue) {
			return asOffsetTime();
		}

		@Override
		public LocalTime asLocalTime(LocalTime defaultValue) {
			return asLocalTime();
		}

		@Override
		public LocalDateTime asLocalDateTime(LocalDateTime defaultValue) {
			return asLocalDateTime();
		}

		@Override
		public OffsetDateTime asOffsetDateTime(OffsetDateTime defaultValue) {
			return asOffsetDateTime();
		}

		@Override
		public ZonedDateTime asZonedDateTime(ZonedDateTime defaultValue) {
			return asZonedDateTime();
		}

		@Override
		public IsoDuration asIsoDuration(IsoDuration defaultValue) {
			return asIsoDuration();
		}

		@Override
		public Point asPoint(Point defaultValue) {
			return asPoint();
		}

		@Override
		public Map<String, Object> asMap(Map<String, Object> defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public <T> Map<String, T> asMap(Function<Value, T> mapFunction, Map<String, T> defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public boolean containsKey(String key) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public Value get(String key) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public Iterable<Value> values() {
			return List.of(delegate);
		}

		@Override
		public <T> Iterable<T> values(Function<Value, T> mapFunction) {
			return List.of(mapFunction.apply(delegate));
		}

		@Override
		public Map<String, Object> asMap() {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public <T> Map<String, T> asMap(Function<Value, T> mapFunction) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public Value get(String key, Value defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public Object get(String key, Object defaultValue) {
			return get(key);
		}

		@Override
		public Number get(String key, Number defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public Entity get(String key, Entity defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public Node get(String key, Node defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public Path get(String key, Path defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public Relationship get(String key, Relationship defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public List<Object> get(String key, List<Object> defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public <T> List<T> get(String key, List<T> defaultValue, Function<Value, T> mapFunc) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public Map<String, Object> get(String key, Map<String, Object> defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public <T> Map<String, T> get(String key, Map<String, T> defaultValue, Function<Value, T> mapFunc) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public int get(String key, int defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public long get(String key, long defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public boolean get(String key, boolean defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public String get(String key, String defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public float get(String key, float defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}

		@Override
		public double get(String key, double defaultValue) {
			throw new Uncoercible(typeSystem.LIST().name(), typeSystem.MAP().name());
		}
	}
}

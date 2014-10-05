package main;

import main.data.Employee;
import plugin.HazelcastPlugin;

import com.facebook.presto.example.ExamplePlugin;
import com.facebook.presto.server.PluginManager;
import com.facebook.presto.server.PrestoServer;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HazelcastPresto extends PrestoServer implements Module, InjectionListener<PluginManager> {

    public static void main(String[] args) {

    	Config config = new Config("local");
    	config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		HazelcastInstance hz = Hazelcast.getOrCreateHazelcastInstance(config);
    	addTestData(hz);

    	new HazelcastPresto().run();
    }

	private static void addTestData(HazelcastInstance hz) {
		IMap<Object, Object> map = hz.getMap("employees");
    	map.put(1, new Employee("Jon", 20, true, 101) );
    	map.put(2, new Employee("Tim", 25, false, 102) );
    	map.put(3, new Employee("Ben", 30, true, 103) );
	}

    @Override
    protected Iterable<? extends Module> getAdditionalModules() {
    	return ImmutableList.of(this);
    }

    @Override
    public void afterInjection(PluginManager pm) {
    	pm.installPlugin(new ExamplePlugin());
    	pm.installPlugin(new HazelcastPlugin());
    }

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void configure(Binder binder) {

		TypeListener listener = new TypeListener() {
			@Override
			public void hear(TypeLiteral type, TypeEncounter encounter) {
				encounter.register(HazelcastPresto.this);
			}
		};

		binder.bindListener(classMatcher(PluginManager.class), listener);
	}

	@SuppressWarnings("rawtypes")
	private static Matcher<TypeLiteral> classMatcher(final Class clazz) {
		Matcher<TypeLiteral> typeMatcher = new AbstractMatcher<TypeLiteral>() {
			@Override
			public boolean matches(TypeLiteral t) {
				return t.getRawType() == clazz;
			};
		};
		return typeMatcher;
	}

}

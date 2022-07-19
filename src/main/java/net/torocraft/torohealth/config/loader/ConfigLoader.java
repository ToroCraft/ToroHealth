package net.torocraft.torohealth.config.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.function.Consumer;

public class ConfigLoader<T extends IConfig> {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final Consumer<T> onLoad;
	private final File file;
	private final T defaultConfig;
	private FileWatcher watcher;

	public ConfigLoader(T defaultConfig, String filename, Consumer<T> onLoad) {
		this.defaultConfig = defaultConfig;
		this.onLoad = onLoad;
		this.file = new File(ConfigFolder.get(), filename);
	}

	public void load() {
		T config = defaultConfig;

		if (!file.exists()) {
			save(config);
		}

		config = read();

		Defaulter.setDefaults(config, defaultConfig.getClass());

		config.update();
		onLoad.accept(config);

		if (config.shouldWatch()) {
			watch(file);
		}
	}

	@SuppressWarnings("unchecked")
	public T read() {
		try (FileReader reader = new FileReader(file)) {
			return (T)GSON.fromJson(reader, defaultConfig.getClass());
		} catch (Exception e) {
			e.printStackTrace();
			return defaultConfig;
		}
	}

	public void save(T config) {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(GSON.toJson(config));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void watch(File file) {
		if (watcher != null) {
			return;
		}

		watcher = FileWatcher.watch(file, () -> load());
	}
}

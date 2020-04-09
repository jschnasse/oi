/* Copyright 2019 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.cjxy.writer.base;

import java.io.File;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.TreeMultimap;
import com.google.common.io.Files;

public class Writer {

	@SafeVarargs
	static public void print(ObjectMapper mapper, Map<String, Object>... maps) {
		for (Map<String, Object> m : maps) {
			System.out.println(write(mapper, m) + "\n");
		}
	}

	static public String write(ObjectMapper mapper, Object obj) {
		try {
			StringWriter w = new StringWriter();
			mapper.writeValue(w, obj);
			return w.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void createFile(ObjectMapper mapper, String path, Map<String, Object> content) {
		try {
			Files.asCharSink(new File(path), Charsets.UTF_8).write(write(mapper, content));
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	static public Map<String, Object> getLvmMounts(List<Map<String, Object>> lvm) {
		if (lvm == null)
			return null;
		Map<String, Object> result = new HashMap<>();
		for (Map<String, Object> p : lvm) {
			result.put(p.get("mount").toString(), p);
		}
		return result;
	}

	static public Map<String, Object> getGroups(List<Map<String, Object>> groups) {
		if (groups == null)
			return null;
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> group = new HashMap<>();
		result.put("group", group);
		for (Map<String, Object> p : groups) {
			group.put(p.get("groupName").toString(), p);
		}
		return result;
	}

	static public Map<String, Object> getShadows(List<Map<String, Object>> passwd) {
		if (passwd == null)
			return null;
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> users = new HashMap<>();
		result.put("user", users);
		for (Map<String, Object> p : passwd) {
			String username = p.get("login").toString();
			users.put(username, p);
		}
		return result;
	}

	static public Map<String, Object> getUsers(List<Map<String, Object>> passwd,
			Map<String, Collection<String>> groupsByUser, Map<String, Object> lvmMap) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> users = new HashMap<>();
		result.put("user", users);
		for (Map<String, Object> p : passwd) {
			String username = p.get("login").toString();
			users.put(username, p);
			p.put("password", "{{ vault_user." + username + ".password }}");
			if (groupsByUser.get(username) != null)
				p.put("groupList", Joiner.on(",").join(groupsByUser.get(username)));

			Map<String, Object> homeMap = new TreeMap<>();
			homeMap.put("path", p.get("home"));
			try {
				Map<String, Object> l = (Map<String, Object>) lvmMap.get(p.get("home"));
				homeMap.put("size", l.get("size"));
				homeMap.put("lv", l.get("device"));
			} catch (Exception e) {
			}

			p.put("homeMap", homeMap);
		}
		return result;
	}

	static public Map<String, Collection<String>> getGroupsByUser(List<Map<String, Object>> groups) {
		TreeMultimap<String, String> result = TreeMultimap.create((a, b) -> {
			return a.compareTo(b);
		}, (a, b) -> {
			return a.compareTo(b);
		});
		for (Map<String, Object> p : groups) {
			for (String user : Splitter.on(',').trimResults().omitEmptyStrings().split("" + p.get("userList"))) {
				result.put(user, p.get("groupName").toString());
			}
		}
		return result.asMap();
	}

	public static void print(ObjectMapper mapper, List<Map<String, Object>> lvm, List<Map<String, Object>> passwds,
			List<Map<String, Object>> groups, List<Map<String, Object>> shadows) {
		Map<String, Collection<String>> groupsByUser = Writer.getGroupsByUser(groups);
		Map<String, Object> lvmInAMap = Writer.getLvmMounts(lvm);
		Map<String, Object> usersInAMap = Writer.getUsers(passwds, groupsByUser, lvmInAMap);
		Map<String, Object> shadowsInAMap = Writer.getShadows(shadows);
		Map<String, Object> groupsInAMap = Writer.getGroups(groups);
		print(mapper, usersInAMap, shadowsInAMap, groupsInAMap);
	}

	public static void gprint(ObjectMapper mapper, Map<String, Object> json) {
		print(mapper, json);
	}

	public static void write(String suffix, ObjectMapper mapper, File dir, List<Map<String, Object>> lvm,
			List<Map<String, Object>> passwds, List<Map<String, Object>> groups, List<Map<String, Object>> shadows) {
		Map<String, Collection<String>> groupsByUser = Writer.getGroupsByUser(groups);
		Map<String, Object> lvmInAMap = Writer.getLvmMounts(lvm);
		Map<String, Object> usersInAMap = Writer.getUsers(passwds, groupsByUser, lvmInAMap);
		Map<String, Object> shadowsInAMap = Writer.getShadows(shadows);
		Map<String, Object> groupsInAMap = Writer.getGroups(groups);
		createFile(mapper, dir.getAbsolutePath() + File.separator + "user." + suffix, usersInAMap);
		createFile(mapper, dir.getAbsolutePath() + File.separator + "group." + suffix, groupsInAMap);
		createFile(mapper, dir.getAbsolutePath() + File.separator + "shadow." + suffix, shadowsInAMap);
	}

}

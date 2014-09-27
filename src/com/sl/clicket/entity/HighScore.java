package com.sl.clicket.entity;

import java.io.Serializable;

public class HighScore implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int level;
	private long score;
	
	public HighScore() {
		super();
	}
	
	public HighScore(int id, int level, long score) {
		super();
		this.id = id;
		this.level = level;
		this.score = score;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}	
}
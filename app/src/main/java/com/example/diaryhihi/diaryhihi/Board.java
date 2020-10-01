package com.example.diaryhihi.diaryhihi;

import java.io.Serializable;

public class Board implements Serializable{
	
	private int boardnum;
	private String title;
	private String text;
	private String wdate;
	
	public Board() {
	}

	public Board(int boardnum, String title, String text, String wdate) {
		this.boardnum = boardnum;
		this.title = title;
		this.text = text;
		this.wdate = wdate;
	}

	public int getBoardnum() {
		return boardnum;
	}

	public void setBoardnum(int boardnum) {
		this.boardnum = boardnum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getWdate() {	return wdate;	}

	public void setWdate(String wdate) {
		this.wdate = wdate;
	}

	@Override
	public String toString() {
		return "Board [boardnum=" + boardnum + ", title=" + title + ", text=" + text + ", wdate=" + wdate + "]";
	}


}

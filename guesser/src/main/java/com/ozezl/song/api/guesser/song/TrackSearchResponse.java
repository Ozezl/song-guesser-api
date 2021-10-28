package com.ozezl.song.api.guesser.song;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackSearchResponse {
	private Tracks tracks;

	public TrackSearchResponse() {
	}

	public Tracks getTracks() {
		return tracks;
	}

	public void setTracks(Tracks tracks) {
		this.tracks = tracks;
	}
}

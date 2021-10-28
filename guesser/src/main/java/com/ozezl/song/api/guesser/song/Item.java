package com.ozezl.song.api.guesser.song;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
	private String name;
	private Integer popularity;
	private String preview_url;

	public Item() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPopularity() {
		return popularity;
	}

	public void setPopularity(Integer popularity) {
		this.popularity = popularity;
	}

	public String getPreview_url() {
		return preview_url;
	}

	public void setPreview_url(String preview_url) {
		this.preview_url = preview_url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((popularity == null) ? 0 : popularity.hashCode());
		result = prime * result + ((preview_url == null) ? 0 : preview_url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (popularity == null) {
			if (other.popularity != null)
				return false;
		} else if (!popularity.equals(other.popularity))
			return false;
		if (preview_url == null) {
			if (other.preview_url != null)
				return false;
		} else if (!preview_url.equals(other.preview_url))
			return false;
		return true;
	}

}

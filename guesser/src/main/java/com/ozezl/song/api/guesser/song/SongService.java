package com.ozezl.song.api.guesser.song;

import java.net.URI;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SongService {
	private static int MAX_OFFSET = 1001; // 1000 (+ 1 because exclusive)
	private static int NUMBER_OF_SONGS_IN_SET = 20;
	private static int NUMBER_OF_MAX_SONGS_PER_SEARCH = 50;

	public static String SEARCH_ADRES = "https://api.spotify.com/v1/search";
	public static String TOKEN_ADRES = "https://accounts.spotify.com/api/token";

	@Value("${secrets.client-id}")
	private String CLIENT_ID;
	@Value("${secrets.client-secret}")
	private String CLIENT_SECRET;

	private String getAuthorizationToken() {
		RestTemplate rest = new RestTemplateBuilder().build();
		HttpHeaders header = new HttpHeaders();
		String body = "grant_type=client_credentials";
		header.add("Authorization", generateBase64Authentication());
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> requestEntity = new HttpEntity<String>(body, header);
		ResponseEntity<TokenMeta> tokenEntity = rest.exchange(URI.create(TOKEN_ADRES), HttpMethod.POST, requestEntity,
				TokenMeta.class);
		return tokenEntity.getBody().getAccess_token();
	}

	private int getRandomOffset() {
		ThreadLocalRandom tl = ThreadLocalRandom.current();
		return tl.nextInt(MAX_OFFSET);
	}

	private String getAuthorizationHeaderValue() {
		return "Bearer " + getAuthorizationToken();
	}

	private String getRandomSearchPattern() {
		String[] a = { "%25a%25", "a%25", "%25a", "%25e%25", "e%25", "%25e", "%25i%25", "i%25", "%25i", "%25o%25",
				"o%25", "%25o", "%25u%25", "u%25", "%25u" };

		return a[ThreadLocalRandom.current().nextInt(a.length)];
	}

	private List<Item> getRandomTracks() {
		String token = getAuthorizationHeaderValue();
		List<Item> songs = getTracks(NUMBER_OF_MAX_SONGS_PER_SEARCH, token);

		while (true) {
			songs = filterTracks(songs);

			if (songs.size() >= NUMBER_OF_SONGS_IN_SET) {
				songs = songs.subList(0, NUMBER_OF_SONGS_IN_SET);
				break;
			}

			songs.addAll(getTracks(NUMBER_OF_MAX_SONGS_PER_SEARCH, token));
		}

		return songs;
	}

	private List<Item> filterTracks(List<Item> songs) {
		return songs.stream().filter(song -> Objects.nonNull(song))
				.filter(song -> Objects.nonNull(song.getPreview_url()))//
				.filter(song -> Objects.nonNull(song.getName()))//
				.filter(song -> Objects.nonNull(song.getPopularity()))//
				.filter(song -> song.getPopularity() > 75)//
				.distinct()//
				.collect(Collectors.toList());
	}

	private List<Item> getTracks(int numberOfTracks, String token) {
		RestTemplate rest = new RestTemplateBuilder().build();
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", token);
		HttpEntity<String> requestEntity = new HttpEntity<String>(header);
		ResponseEntity<TrackSearchResponse> response = rest.exchange(
				URI.create(SEARCH_ADRES + "?q=" + getRandomSearchPattern() + "&offset=" + getRandomOffset()
						+ "&type=track" + "&limit=" + numberOfTracks),
				HttpMethod.GET, requestEntity, TrackSearchResponse.class);
		return response.getBody().getTracks().getItems();
	}

	private String generateBase64Authentication() {
		return "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
	}

	public List<Item> getSongs() {
		return getRandomTracks();
	}
}

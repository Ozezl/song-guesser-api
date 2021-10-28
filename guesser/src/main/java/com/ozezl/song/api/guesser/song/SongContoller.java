package com.ozezl.song.api.guesser.song;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class SongContoller {

	SongService songService;

	@Autowired
	public SongContoller(SongService songService) {
		this.songService = songService;
	}

	@GetMapping("/v1/song")
	public List<Item> getSongs() {
		return songService.getSongs();
	}
}

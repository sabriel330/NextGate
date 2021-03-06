package com.nextgate.assesment.rest;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextgate.assesment.datatypes.Album;
import com.nextgate.assesment.datatypes.Sex;
import com.nextgate.assesment.datatypes.Singer;
import com.nextgate.assesment.datatypes.User;
import com.nextgate.assesment.service.AlbumService;
import com.nextgate.assesment.service.SingerService;
import com.nextgate.assesment.service.UserService;

@RestController
@RequestMapping("/api")
public class RestAPIController {

	@Autowired
	private AlbumService mAlbumService;
	
	@Autowired
	private SingerService mSingerService;
	
	@Autowired
	private UserService mUserService;
	
	private ObjectMapper mObjectMapper;
	
	@GetMapping("/albums")
	public List<Album>getAlbums(){
		List<Album> results = mAlbumService.allAlbums();
		System.out.println("Successfully fetched all albums");
		return results;
	}
	
	@GetMapping("/singers")
	public List<Singer> getSingers(){
		List<Singer> results = mSingerService.allSingers();
		System.out.println("Successfully fetched all singers");
		return results;
	}
	
	@PostMapping("/login")
	@ResponseBody
	public boolean login(@RequestBody User aUser ) {
		System.out.println(String.format("Confirming that @RequestBody is mapping: %1$s %2$s", aUser.getUsername(), aUser.getPassword()));
		return mUserService.Login(aUser);
	}
	
	@PostMapping("/search/singers")
	@ResponseBody
	public List<Singer> searchSingers(@RequestBody String aSearchTerm){
		aSearchTerm=aSearchTerm.replaceAll("\"", "");
		List<Singer> singerSearchResults=  mSingerService.Search(aSearchTerm);
		return singerSearchResults;
	}
	
	@PostMapping("/search/albums")
	@ResponseBody
	public List<Album> searchAlbums(@RequestBody String aSearchTerm){
		aSearchTerm=aSearchTerm.replaceAll("\"", "");
		List<Album> albumSearchResults=  mAlbumService.Search(aSearchTerm);
		return albumSearchResults;
	}
	
	@PostMapping("/add/singer")
	@ResponseBody
	public void addSinger(@RequestBody String payload) {
		try {
			JSONObject json  = new JSONObject(payload);
			String singerName=json.getString("singerName");
			String singerCompany = json.getString("singerCompany");
			String singerDOB=json.getString("singerDOB");
			mSingerService.addSinger(new Singer(singerName, singerCompany, Sex.valueOf(json.getString("singerSex")), singerDOB));
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping("/add/album")
	@ResponseBody
	public void addAlbum(@RequestBody String payload) {
		
		
		try {
			JSONObject json  = new JSONObject(payload);
			String albumName=json.getString("albumName");
			String albumCompany = json.getString("albumCompany");
			String albumSinger=json.getString("albumSinger");
			String albumYear=json.getString("albumYear");
			mAlbumService.addAlbum(new Album(albumName, albumCompany, albumSinger, albumYear));
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

}

package dhbw.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.detail.track.DetailsTrack;
import dhbw.pojo.result.search.SearchResult;
import dhbw.pojo.result.search.SearchResultList;
import dhbw.pojo.search.GeneralItem;
import dhbw.pojo.search.album.SearchAlbum;
import dhbw.pojo.search.artist.SearchArtist;
import dhbw.pojo.search.track.Item;
import dhbw.pojo.search.track.SearchTrack;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;
import dhbw.spotify.WrongRequestTypeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class SearchController {

    @GetMapping("/search")
    public SearchResult search(@RequestParam String query, @RequestParam RequestCategory type) {
        SearchResult response = new SearchResult();
        try {
            SpotifyRequest sRequest = new SpotifyRequest(RequestType.SEARCH);
            Optional<String> optional = sRequest.performeRequestSearch(type, query);
            if(optional.isPresent()) {
                String searchParam = optional.get();
                List<SearchResultList> responseList = new ArrayList<>();
                response.setResults(responseList);
                ObjectMapper mapper = new ObjectMapper();
                switch(type) {
                    case TRACK:
                        SearchTrack sTrack = mapper.readValue(searchParam, SearchTrack.class);
                        for (GeneralItem item : sTrack.getTracks().getItems())
                            setResponse(item, responseList);
                        break;
                    case ALBUM:
                        SearchAlbum sAlbum = mapper.readValue(searchParam, SearchAlbum.class);
                        for (GeneralItem item : sAlbum.getAlbums().getItems())
                            setResponse(item, responseList);
                        break;
                    case ARTIST:
                        SearchArtist sArtist = mapper.readValue(searchParam, SearchArtist.class);
                        for (GeneralItem item : sArtist.getArtists().getItems())
                            setResponse(item, responseList);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private void setResponse(GeneralItem item, List<SearchResultList> responseList){
        SearchResultList si = new SearchResultList();
        si.setId(item.getId());
        si.setPlayLink(item.getUri());
        si.setTitle(item.getName());
        si.setDescription(item.getType());
        responseList.add(si);
    }
}

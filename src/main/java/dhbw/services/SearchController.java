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
                        for (dhbw.pojo.search.track.Item itemtr : sTrack.getTracks().getItems())
                            setResponse(itemtr, responseList, Long.toString(itemtr.getDurationMs()/1000) );
                        break;
                    case ALBUM:
                        SearchAlbum sAlbum = mapper.readValue(searchParam, SearchAlbum.class);
                        for (dhbw.pojo.search.album.Item itemAl : sAlbum.getAlbums().getItems())
                            setResponse(itemAl, responseList, itemAl.getReleaseDate());
                        break;
                    case ARTIST:
                        SearchArtist sArtist = mapper.readValue(searchParam, SearchArtist.class);
                        for (dhbw.pojo.search.artist.Item itemAr : sArtist.getArtists().getItems())
                            setResponse(itemAr, responseList, itemAr.getPopularity().toString());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private void setResponse(GeneralItem item, List<SearchResultList> responseList, String additional){
        SearchResultList si = new SearchResultList();
        si.setId(item.getId());
        si.setPlayLink(item.getUri());
        si.setTitle(item.getName());
        si.setDescription(item.getType());
        si.setAdditionalInfo(additional);
        responseList.add(si);
    }
}

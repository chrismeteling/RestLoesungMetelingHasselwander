package dhbw.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.detail.Details;
import dhbw.pojo.detail.album.DetailsAlbum;
import dhbw.pojo.detail.artist.DetailsArtist;
import dhbw.pojo.detail.track.DetailsTrack;
import dhbw.pojo.result.detail.DetailResult;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
public class DetailController {

    @GetMapping("/detail/{id}")
    public Object getDetails(@PathVariable String id, @RequestParam RequestCategory type) { //ob RequestCategory oder String
        DetailResult response = new DetailResult();
        try {
            SpotifyRequest sRequest = new SpotifyRequest(RequestType.DETAIL);
            Optional<String> optional = sRequest.performeRequestDetail(type, id);
            if(optional.isPresent()) {
                String searchParam = optional.get();
                ObjectMapper mapper = new ObjectMapper();
                switch (type) {
                    case ALBUM:
                        setResponse(mapper.readValue(searchParam, DetailsAlbum.class), response);
                        break;
                    case TRACK:
                        setResponse(mapper.readValue(searchParam, DetailsTrack.class), response);
                        break;
                    case ARTIST:
                        setResponse(mapper.readValue(searchParam, DetailsArtist.class), response);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private void setResponse(Details details, DetailResult response){
        response.setTitle(details.getName());
        response.setInfo(details.getType());
    }
}

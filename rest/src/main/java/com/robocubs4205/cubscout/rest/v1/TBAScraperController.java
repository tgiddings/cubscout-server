package com.robocubs4205.cubscout.rest.v1;

import com.robocubs4205.cubscout.tbascraper.TBAScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tbascraper")
public class TBAScraperController {
    private final TBAScraper scraper;

    @Autowired
    public TBAScraperController(TBAScraper scraper){

        this.scraper = scraper;
    }
    @RequestMapping(method = RequestMethod.POST)
    public void scrape(){
        scraper.getEvents();
    }
}

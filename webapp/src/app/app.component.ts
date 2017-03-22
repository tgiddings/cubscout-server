import {Component, OnInit, Pipe, PipeTransform, NgModule} from '@angular/core';
import {ScorecardSection, FieldSection, TitleSection, ParagraphSection, Scorecard} from "./scorecard";
import {ScorecardService} from "./scorecard.service";
import {Observable} from "rxjs";
import {Game} from "./game";
import {GameService} from "./game.service";
import {EventService} from "./event.service";
import {ApiRootService} from "./api-root.service";
import {ApiRoot} from "./api-root";

@Component({
             selector: 'app-root',
             templateUrl: './app.component.html',
             styleUrls: ['./app.component.css']
           })
export class AppComponent implements OnInit {
  title = 'app works!';
  games:Game[] = [];
  selectedGame: Game = null;
  events:Event[] = [];
  selectedEvent: Event = null;
  scorecard: Scorecard = null;

  apiRoot:ApiRoot;

  constructor(private scorecardService: ScorecardService,private gameService:GameService,
              private eventService:EventService, private apiRootService:ApiRootService) {}

  ngOnInit() {
    /*this.scorecardService.getScorecardById(1).subscribe(
      scorecard => {
        this.scorecard = scorecard;
        console.log(JSON.stringify(scorecard));
      },
      error => console.log(error.toString()));*/

    this.apiRootService.getRoot().subscribe(root=>{
      this.apiRoot = root;
      console.log(JSON.stringify(root))

      this.gameService.getAllGames(root).subscribe(games=>{
        this.games = games;
        console.log(JSON.stringify(games));
      });
    });
  }

  isFieldSection(section: ScorecardSection): boolean {return section.sectionType=="field"}

  toFieldSection(section: ScorecardSection): FieldSection {return <FieldSection><any>section}

  isTitleSection(section: ScorecardSection): boolean {return section.sectionType=="title"}

  toTitleSection(section: ScorecardSection): TitleSection {return <TitleSection><any>section}

  isParagraphSection(section: ScorecardSection): boolean {return section.sectionType=="paragraph"}

  toParagraphSection(section: ScorecardSection): ParagraphSection {return <ParagraphSection><any>section}

  selectedGameChange():void{
    this.scorecardService.getScorecardsByGame(this.selectedGame).subscribe(
      scorecards => {
        this.scorecard = scorecards[0];
        console.log(JSON.stringify(scorecards));
      },
      error => console.log(error.toString()));

    this.eventService.getEventsByGame(this.selectedGame).subscribe(
      events=>{
        this.events = events;
      },
      error=>console.log(error.toString())
    );

    console.log("changed");
  }
  submitButtonPressed():void{
    console.log("pressed!");
  }
}
@Pipe({name:"sortScorecardSection",pure:true})
export class sortScorecardSectionPipe implements PipeTransform{
  transform(value:ScorecardSection[]):ScorecardSection[]{
    return value.sort((section1,section2)=>{
      if(section1.index==section2.index) return 0;
      return section1.index>section2.index?1:-1;
    })
  }
}

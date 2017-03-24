import { Injectable } from '@angular/core';
import {Result, ResultSubmission} from "./result";
import {Observable} from "rxjs";
import {Match} from "./match";
import {Link} from "./link";
import {Http, RequestOptions, Headers, Response} from "@angular/http";

@Injectable()
export class ResultService {

  submitResult(result:ResultSubmission,cmatch:Match):Observable<Result>{
    let link: Link = cmatch.links.find(link => link.rel == "results");
    if (link == null) return Observable.throw("game does not have link with rel \"results\"");
    return this.http.post(link.href,
                   result,
                   new RequestOptions({
                     headers: new Headers([
                       {'accept': 'application/vnd.robocubs-v1+json'}
                     ])
                   })).map(res => res.json()).catch(error => {
      let errMsg: string;
      if (error instanceof Response) {
        const body = error.json() || '';
        const err = body.error || JSON.stringify(body);
        errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
        errMsg += JSON.stringify(error);
      } else {
        errMsg = error.message ? error.message : error.toString();
      }
      console.error("failed to retrieve scorecard");
      console.error(errMsg);
      return Observable.throw(errMsg);
    });
  }

  constructor(private http:Http) { }

}

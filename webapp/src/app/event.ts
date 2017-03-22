import {Link} from "./link";
export class Event {
  id:number;
  shortName:string;
  address:string;

  links:Link[];

  constructor(values: Object = {}) {
    Object.assign(this, values);
  }
}

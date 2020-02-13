export interface IChatroom {
  id?: number;
  name?: string;
}

export class Chatroom implements IChatroom {
  constructor(public id?: number, public name?: string) {}
}

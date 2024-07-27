import { Types } from 'mongoose';

export class CompleteChallengeDto {
  challengeId: Types.ObjectId;
  title: string;
  constructor(challengeId: Types.ObjectId, title: string) {
    this.challengeId = challengeId;
    this.title = title;
  }
}

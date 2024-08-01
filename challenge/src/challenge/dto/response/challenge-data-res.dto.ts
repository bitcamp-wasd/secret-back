import { Types } from 'mongoose';
import { State } from 'src/challenge/state.enum';

export class ChallengeBannerResponseDto {
  challengeId: Types.ObjectId;
  title: string;
  endDate: Date;
  voteEndDate: Date;
  state: State;

  constructor(
    challengeId: Types.ObjectId,
    title: string,
    endDate: Date,
    voteEndDate: Date,
    state: State,
  ) {
    this.challengeId = challengeId;
    this.title = title;
    this.endDate = endDate;
    this.voteEndDate = voteEndDate;
    this.state = state;
  }
}

import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { IsEnum } from 'class-validator';

import { State } from './state.enum';
import * as mongoosePaginate from 'mongoose-paginate-v2';
import { Types } from 'mongoose';

@Schema({ timestamps: true, collection: 'challenge' })
export class Challenge {
  @Prop()
  title: string;

  @Prop()
  numberOfPeople: number;

  @Prop()
  endDate: Date;

  @Prop()
  voteEndDate: Date;

  @Prop({ default: 0 })
  count: number;

  @Prop({ default: 0 })
  recruited: number;

  @Prop({ type: String, enum: State, default: State.RECRUITMENT })
  @IsEnum(State)
  state: State;

  @Prop({ default: [], type: [{ type: Types.ObjectId }], ref: 'ChallengeList' })
  challengeList: Types.ObjectId[];
}

const schema = SchemaFactory.createForClass(Challenge);
schema.plugin(mongoosePaginate);
export const ChallengeSchema = schema;

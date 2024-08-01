import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import { UserAuthDto } from './userAuth.dto';
import { plainToInstance } from 'class-transformer';

export const UserAuth = createParamDecorator(
  (data: string, ctx: ExecutionContext): UserAuthDto => {
    const request = ctx.switchToHttp().getRequest();

    const userAuth: UserAuthDto = plainToInstance(
      UserAuthDto,
      JSON.parse(
        Buffer.from(request.headers[data], 'base64').toString('utf-8'),
      ),
    );

    return userAuth;
  },
);

import { HttpService } from '@nestjs/axios';
import { Injectable } from '@nestjs/common';
import { firstValueFrom, map } from 'rxjs';
import { SpringCloudConfig } from 'src/config/cloud.config';

@Injectable()
export class RestApiService {
  url: string;

  constructor(
    private readonly httpService: HttpService,
    private readonly springCloudConfig: SpringCloudConfig,
  ) {
    this.url = springCloudConfig.get('url');
  }

  async getUser(users: number[]) {
    try {
      const data = this.httpService
        .post(`${this.url}api/user/listInfo`, {
          userId: users,
        })
        .pipe(map((response) => response.data));

      const result = await firstValueFrom(data);
      return result;
    } catch (error) {
      console.log('error: ' + error);
    }
  }
}

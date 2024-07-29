import * as SpringConfigClient from 'cloud-config-client';
import { Config, Options } from 'cloud-config-client';

export const configProviders = [
  {
    provide: 'config',
    useFactory: async () => {
      const configOptions: Options = {
        endpoint: 'http://10.0.8.8:8082',
        name: 'challenge',
        profiles: ['dev'],
        label: 'prod',
      };

      try {
        const config: Config = await SpringConfigClient.load(
          configOptions,
        ).then((configYaml) => {
          return configYaml;
        });

        return config;
      } catch (error) {
        console.error(error);
      }
    },
  },
];

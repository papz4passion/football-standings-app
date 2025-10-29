import axios, { AxiosInstance, AxiosError } from 'axios';
import {
  Country,
  League,
  Team,
  Standing,
  HealthResponse,
  CacheResponse,
  OfflineModeResponse,
  ApiError,
} from '../types';

class FootballService {
  private api: AxiosInstance;
  private readonly baseURL: string;
  private readonly timeout: number;

  constructor() {
    this.baseURL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';
    this.timeout = Number(process.env.REACT_APP_API_TIMEOUT) || 30000;

    this.api = axios.create({
      baseURL: this.baseURL,
      timeout: this.timeout,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Request interceptor
    this.api.interceptors.request.use(
      (config) => {
        console.log(`[API Request] ${config.method?.toUpperCase()} ${config.url}`);
        return config;
      },
      (error) => {
        console.error('[API Request Error]', error);
        return Promise.reject(error);
      }
    );

    // Response interceptor
    this.api.interceptors.response.use(
      (response) => {
        console.log(`[API Response] ${response.status} ${response.config.url}`);
        return response;
      },
      (error: AxiosError) => {
        console.error('[API Response Error]', error);
        return Promise.reject(this.handleError(error));
      }
    );
  }

  private handleError(error: AxiosError): ApiError {
    if (error.response) {
      // Server responded with error status
      return {
        message: (error.response.data as { message?: string })?.message || error.message,
        status: error.response.status,
        details: JSON.stringify(error.response.data),
      };
    } else if (error.request) {
      // Request made but no response
      return {
        message: 'No response from server. Please check your connection.',
        details: 'Network error or server is down',
      };
    } else {
      // Error in request setup
      return {
        message: error.message || 'An unexpected error occurred',
        details: 'Request configuration error',
      };
    }
  }

  /**
   * Fetch all available countries
   */
  async getCountries(): Promise<Country[]> {
    try {
      const response = await this.api.get<Country[]>('/api/countries');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Fetch leagues by country ID
   */
  async getLeagues(countryId: string): Promise<League[]> {
    try {
      const response = await this.api.get<League[]>('/api/leagues', {
        params: { country_id: countryId },
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Fetch teams by league ID
   */
  async getTeams(leagueId: string): Promise<Team[]> {
    try {
      const response = await this.api.get<Team[]>('/api/teams', {
        params: { league_id: leagueId },
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Fetch standings by league ID with optional team name filter
   */
  async getStandings(leagueId: string, teamName?: string): Promise<Standing[]> {
    try {
      const params: { league_id: string; team_name?: string } = {
        league_id: leagueId,
      };
      if (teamName) {
        params.team_name = teamName;
      }
      const response = await this.api.get<Standing[]>('/api/standings', { params });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Toggle offline mode
   */
  async toggleOfflineMode(enabled: boolean): Promise<OfflineModeResponse> {
    try {
      const response = await this.api.post<OfflineModeResponse>('/api/offline-mode', null, {
        params: { enabled },
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Clear cache
   */
  async clearCache(): Promise<CacheResponse> {
    try {
      const response = await this.api.delete<CacheResponse>('/api/cache');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Health check
   */
  async getHealth(): Promise<HealthResponse> {
    try {
      const response = await this.api.get<HealthResponse>('/actuator/health');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
}

// Export singleton instance
const footballServiceInstance = new FootballService();

// eslint-disable-next-line import/no-anonymous-default-export
export default footballServiceInstance;

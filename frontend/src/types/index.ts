// Country types
export interface Country {
  country_id: string;
  country_name: string;
  country_logo: string;
}

// League types
export interface League {
  country_id: string;
  country_name: string;
  league_id: string;
  league_name: string;
  league_season: string;
  league_logo: string;
  country_logo: string;
}

// Team types
export interface Team {
  team_key: string;
  team_name: string;
  team_country: string;
  team_founded: string;
  team_badge: string;
  venue?: Venue;
}

export interface Venue {
  venue_name: string;
  venue_address: string;
  venue_city: string;
  venue_capacity: string;
  venue_surface: string;
}

// Standing types
export interface Standing {
  country_name: string;
  league_id: string;
  league_name: string;
  team_id: string;
  team_name: string;
  overall_promotion: string;
  overall_league_position: string;
  overall_league_payed: string;
  overall_league_W: string;
  overall_league_D: string;
  overall_league_L: string;
  overall_league_GF: string;
  overall_league_GA: string;
  overall_league_PTS: string;
  home_league_position: string;
  home_promotion: string;
  home_league_payed: string;
  home_league_W: string;
  home_league_D: string;
  home_league_L: string;
  home_league_GF: string;
  home_league_GA: string;
  home_league_PTS: string;
  away_league_position: string;
  away_promotion: string;
  away_league_payed: string;
  away_league_W: string;
  away_league_D: string;
  away_league_L: string;
  away_league_GF: string;
  away_league_GA: string;
  away_league_PTS: string;
  league_round: string;
  team_badge: string;
  fk_stage_key: string;
  stage_name: string;
}

// API Response types
export interface ApiResponse<T> {
  data: T;
  status: number;
  statusText: string;
}

export interface HealthResponse {
  status: string;
  details?: Record<string, unknown>;
}

export interface CacheResponse {
  message: string;
  cleared: boolean;
}

export interface OfflineModeResponse {
  message: string;
  enabled: boolean;
}

// Error types
export interface ApiError {
  message: string;
  status?: number;
  details?: string;
}

// Component Props types
export interface DropdownOption {
  value: string;
  label: string;
  data?: unknown;
}

export interface DropdownProps<T> {
  label: string;
  options: T[];
  value: string;
  onChange: (value: string) => void;
  disabled?: boolean;
  loading?: boolean;
  placeholder?: string;
  displayField: keyof T;
  valueField: keyof T;
  renderOption?: (option: T) => string;
}

export interface StandingsTableProps {
  standings: Standing[];
}

export interface LoadingSpinnerProps {
  size?: 'small' | 'medium' | 'large';
  text?: string;
}

export interface ErrorMessageProps {
  message: string;
  onRetry?: () => void;
}

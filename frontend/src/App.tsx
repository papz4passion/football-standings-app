import React, { useState, useEffect, useCallback } from 'react';
import footballService from './services/footballService';
import Dropdown from './components/Dropdown';
import StandingsTable from './components/StandingsTable';
import LoadingSpinner from './components/LoadingSpinner';
import ErrorMessage from './components/ErrorMessage';
import { Country, League, Team, Standing, ApiError } from './types';
import './App.css';

const App: React.FC = () => {
  // State for dropdowns
  const [countries, setCountries] = useState<Country[]>([]);
  const [leagues, setLeagues] = useState<League[]>([]);
  const [teams, setTeams] = useState<Team[]>([]);
  const [standings, setStandings] = useState<Standing[]>([]);

  // Selected values
  const [selectedCountry, setSelectedCountry] = useState<string>('');
  const [selectedLeague, setSelectedLeague] = useState<string>('');
  const [selectedTeam, setSelectedTeam] = useState<string>('');

  // Loading states
  const [loadingCountries, setLoadingCountries] = useState<boolean>(false);
  const [loadingLeagues, setLoadingLeagues] = useState<boolean>(false);
  const [loadingTeams, setLoadingTeams] = useState<boolean>(false);
  const [loadingStandings, setLoadingStandings] = useState<boolean>(false);

  // Error states
  const [error, setError] = useState<string | null>(null);

  // Offline mode
  const [offlineMode, setOfflineMode] = useState<boolean>(false);
  const [togglingOfflineMode, setTogglingOfflineMode] = useState<boolean>(false);

  // Selected data info
  const [selectedCountryData, setSelectedCountryData] = useState<Country | null>(null);
  const [selectedLeagueData, setSelectedLeagueData] = useState<League | null>(null);
  const [selectedTeamData, setSelectedTeamData] = useState<Team | null>(null);

  // Fetch countries on mount
  useEffect(() => {
    fetchCountries();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Fetch countries
  const fetchCountries = useCallback(async (): Promise<void> => {
    try {
      setLoadingCountries(true);
      setError(null);
      const data = await footballService.getCountries();
      setCountries(data);
    } catch (err) {
      const error = err as ApiError;
      setError(error.message);
      console.error('Error fetching countries:', err);
    } finally {
      setLoadingCountries(false);
    }
  }, []);

  // Handle country selection
  const handleCountryChange = async (countryId: string): Promise<void> => {
    setSelectedCountry(countryId);
    setSelectedLeague('');
    setSelectedTeam('');
    setLeagues([]);
    setTeams([]);
    setStandings([]);
    setSelectedCountryData(null);
    setSelectedLeagueData(null);
    setSelectedTeamData(null);

    if (!countryId) return;

    // Find selected country data
    const country = countries.find((c) => c.country_id === countryId);
    setSelectedCountryData(country || null);

    try {
      setLoadingLeagues(true);
      setError(null);
      const data = await footballService.getLeagues(countryId);
      setLeagues(data);
    } catch (err) {
      const error = err as ApiError;
      setError(error.message);
      console.error('Error fetching leagues:', err);
    } finally {
      setLoadingLeagues(false);
    }
  };

  // Handle league selection
  const handleLeagueChange = async (leagueId: string): Promise<void> => {
    setSelectedLeague(leagueId);
    setSelectedTeam('');
    setTeams([]);
    setStandings([]);
    setSelectedLeagueData(null);
    setSelectedTeamData(null);

    if (!leagueId) return;

    // Find selected league data
    const league = leagues.find((l) => l.league_id === leagueId);
    setSelectedLeagueData(league || null);

    // Fetch teams and standings in parallel
    try {
      setLoadingTeams(true);
      setLoadingStandings(true);
      setError(null);

      const [teamsData, standingsData] = await Promise.all([
        footballService.getTeams(leagueId),
        footballService.getStandings(leagueId),
      ]);

      setTeams(teamsData);
      setStandings(standingsData);
    } catch (err) {
      const error = err as ApiError;
      setError(error.message);
      console.error('Error fetching teams/standings:', err);
    } finally {
      setLoadingTeams(false);
      setLoadingStandings(false);
    }
  };

  // Handle team selection (filter standings)
  const handleTeamChange = (teamName: string): void => {
    setSelectedTeam(teamName);

    if (!teamName) {
      // Show all standings if no team selected
      setSelectedTeamData(null);
      return;
    }

    // Find selected team data
    const team = teams.find((t) => t.team_name === teamName);
    setSelectedTeamData(team || null);
  };

  // Filter standings by selected team
  const filteredStandings = selectedTeam
    ? standings.filter((s) =>
        s.team_name.toLowerCase().includes(selectedTeam.toLowerCase())
      )
    : standings;

  // Toggle offline mode
  const handleToggleOfflineMode = async (): Promise<void> => {
    try {
      setTogglingOfflineMode(true);
      setError(null);
      const newOfflineMode = !offlineMode;
      await footballService.toggleOfflineMode(newOfflineMode);
      setOfflineMode(newOfflineMode);
    } catch (err) {
      const error = err as ApiError;
      setError(error.message);
      console.error('Error toggling offline mode:', err);
    } finally {
      setTogglingOfflineMode(false);
    }
  };

  // Clear cache
  const handleClearCache = async (): Promise<void> => {
    if (!window.confirm('Are you sure you want to clear the cache?')) {
      return;
    }

    try {
      setError(null);
      await footballService.clearCache();

      // Reset all selections
      setSelectedCountry('');
      setSelectedLeague('');
      setSelectedTeam('');
      setLeagues([]);
      setTeams([]);
      setStandings([]);
      setSelectedCountryData(null);
      setSelectedLeagueData(null);
      setSelectedTeamData(null);

      // Reload countries
      fetchCountries();
    } catch (err) {
      const error = err as ApiError;
      setError(error.message);
      console.error('Error clearing cache:', err);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-blue-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <div className="flex items-center justify-between">
            <div className="flex items-center">
              <svg
                className="h-10 w-10 text-primary-600"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"
                />
              </svg>
              <div className="ml-4">
                <h1 className="text-3xl font-bold text-gray-900">
                  Football Standings
                </h1>
                <p className="text-sm text-gray-500 mt-1">
                  View team standings in league football matches
                </p>
              </div>
            </div>
            <div className="flex items-center space-x-4">
              <button
                onClick={handleToggleOfflineMode}
                disabled={togglingOfflineMode}
                className={`
                  inline-flex items-center px-4 py-2 border rounded-lg text-sm font-medium
                  transition-all duration-200
                  ${
                    offlineMode
                      ? 'bg-yellow-50 border-yellow-300 text-yellow-700 hover:bg-yellow-100'
                      : 'bg-white border-gray-300 text-gray-700 hover:bg-gray-50'
                  }
                  ${togglingOfflineMode ? 'opacity-50 cursor-not-allowed' : ''}
                  focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500
                `}
              >
                <svg
                  className="mr-2 h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M3 15a4 4 0 004 4h9a5 5 0 10-.1-9.999 5.002 5.002 0 10-9.78 2.096A4.001 4.001 0 003 15z"
                  />
                </svg>
                {offlineMode ? 'Offline Mode: ON' : 'Offline Mode: OFF'}
              </button>
              <button
                onClick={handleClearCache}
                className="inline-flex items-center px-4 py-2 border border-gray-300 rounded-lg text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
              >
                <svg
                  className="mr-2 h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                  />
                </svg>
                Clear Cache
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Error Message */}
        {error && (
          <div className="mb-6">
            <ErrorMessage message={error} onRetry={fetchCountries} />
          </div>
        )}

        {/* Selection Info Card */}
        {(selectedCountryData || selectedLeagueData || selectedTeamData) && (
          <div className="mb-6 bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">
              Current Selection
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {selectedCountryData && (
                <div className="flex items-start space-x-3">
                  <div className="flex-shrink-0">
                    {selectedCountryData.country_logo && (
                      <img
                        src={selectedCountryData.country_logo}
                        alt={selectedCountryData.country_name}
                        className="h-12 w-12 rounded-lg object-cover"
                        onError={(e: React.SyntheticEvent<HTMLImageElement>) => {
                          e.currentTarget.style.display = 'none';
                        }}
                      />
                    )}
                  </div>
                  <div>
                    <p className="text-xs text-gray-500 uppercase tracking-wide">
                      Country
                    </p>
                    <p className="text-sm font-medium text-gray-900">
                      ({selectedCountryData.country_id}) -{' '}
                      {selectedCountryData.country_name}
                    </p>
                  </div>
                </div>
              )}
              {selectedLeagueData && (
                <div className="flex items-start space-x-3">
                  <div className="flex-shrink-0">
                    {selectedLeagueData.league_logo && (
                      <img
                        src={selectedLeagueData.league_logo}
                        alt={selectedLeagueData.league_name}
                        className="h-12 w-12 rounded-lg object-cover"
                        onError={(e: React.SyntheticEvent<HTMLImageElement>) => {
                          e.currentTarget.style.display = 'none';
                        }}
                      />
                    )}
                  </div>
                  <div>
                    <p className="text-xs text-gray-500 uppercase tracking-wide">
                      League
                    </p>
                    <p className="text-sm font-medium text-gray-900">
                      ({selectedLeagueData.league_id}) -{' '}
                      {selectedLeagueData.league_name}
                    </p>
                    {selectedLeagueData.league_season && (
                      <p className="text-xs text-gray-500">
                        {selectedLeagueData.league_season}
                      </p>
                    )}
                  </div>
                </div>
              )}
              {selectedTeamData && (
                <div className="flex items-start space-x-3">
                  <div className="flex-shrink-0">
                    {selectedTeamData.team_badge && (
                      <img
                        src={selectedTeamData.team_badge}
                        alt={selectedTeamData.team_name}
                        className="h-12 w-12 rounded-lg object-cover"
                        onError={(e: React.SyntheticEvent<HTMLImageElement>) => {
                          e.currentTarget.style.display = 'none';
                        }}
                      />
                    )}
                  </div>
                  <div>
                    <p className="text-xs text-gray-500 uppercase tracking-wide">
                      Team
                    </p>
                    <p className="text-sm font-medium text-gray-900">
                      ({selectedTeamData.team_key}) - {selectedTeamData.team_name}
                    </p>
                    {selectedTeamData.team_country && (
                      <p className="text-xs text-gray-500">
                        {selectedTeamData.team_country}
                      </p>
                    )}
                  </div>
                </div>
              )}
            </div>
          </div>
        )}

        {/* Filters Section */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-8">
          <h2 className="text-lg font-semibold text-gray-900 mb-6">
            Search Filters
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {/* Country Dropdown */}
            <Dropdown
              label="Country"
              options={countries}
              value={selectedCountry}
              onChange={handleCountryChange}
              loading={loadingCountries}
              placeholder="Select a country"
              displayField="country_name"
              valueField="country_id"
              renderOption={(country: Country) =>
                `(${country.country_id}) - ${country.country_name}`
              }
            />

            {/* League Dropdown */}
            <Dropdown
              label="League"
              options={leagues}
              value={selectedLeague}
              onChange={handleLeagueChange}
              disabled={!selectedCountry}
              loading={loadingLeagues}
              placeholder="Select a league"
              displayField="league_name"
              valueField="league_id"
              renderOption={(league: League) =>
                `(${league.league_id}) - ${league.league_name}`
              }
            />

            {/* Team Dropdown */}
            <Dropdown
              label="Team (Optional Filter)"
              options={teams}
              value={selectedTeam}
              onChange={handleTeamChange}
              disabled={!selectedLeague}
              loading={loadingTeams}
              placeholder="All teams"
              displayField="team_name"
              valueField="team_name"
              renderOption={(team: Team) => `(${team.team_key}) - ${team.team_name}`}
            />
          </div>
        </div>

        {/* Standings Section */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-200 bg-gray-50">
            <h2 className="text-lg font-semibold text-gray-900">
              League Standings
              {selectedLeagueData && (
                <span className="ml-2 text-sm font-normal text-gray-500">
                  - {selectedLeagueData.league_name}
                  {selectedTeam && ` (Filtered by ${selectedTeam})`}
                </span>
              )}
            </h2>
            {filteredStandings.length > 0 && (
              <p className="text-sm text-gray-500 mt-1">
                Showing {filteredStandings.length} team
                {filteredStandings.length !== 1 ? 's' : ''}
              </p>
            )}
          </div>

          <div className="p-6">
            {loadingStandings ? (
              <LoadingSpinner text="Loading standings..." />
            ) : (
              <StandingsTable standings={filteredStandings} />
            )}
          </div>
        </div>

        {/* Instructions */}
        {!selectedCountry && !loadingCountries && !error && (
          <div className="mt-8 bg-blue-50 border border-blue-200 rounded-lg p-6">
            <div className="flex items-start">
              <div className="flex-shrink-0">
                <svg
                  className="h-6 w-6 text-blue-400"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                  />
                </svg>
              </div>
              <div className="ml-3">
                <h3 className="text-sm font-medium text-blue-800">
                  Getting Started
                </h3>
                <div className="mt-2 text-sm text-blue-700">
                  <ol className="list-decimal list-inside space-y-1">
                    <li>Select a country from the dropdown</li>
                    <li>Choose a league from the available leagues</li>
                    <li>
                      Optionally filter by team to see specific standings
                    </li>
                    <li>View detailed standings with statistics</li>
                  </ol>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="mt-12 bg-white border-t border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <p className="text-center text-sm text-gray-500">
            Football Standings Application © 2025 | Powered by API Football
          </p>
        </div>
      </footer>
    </div>
  );
};

export default App;

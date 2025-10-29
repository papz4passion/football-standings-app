import React from 'react';
import { StandingsTableProps, Standing } from '../types';

const StandingsTable: React.FC<StandingsTableProps> = ({ standings }) => {
  if (standings.length === 0) {
    return (
      <div className="text-center py-12">
        <svg
          className="mx-auto h-12 w-12 text-gray-400"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
          />
        </svg>
        <h3 className="mt-2 text-sm font-medium text-gray-900">No standings available</h3>
        <p className="mt-1 text-sm text-gray-500">
          Please select a league to view standings.
        </p>
      </div>
    );
  }

  const getPositionColor = (position: string): string => {
    const pos = parseInt(position, 10);
    if (pos <= 4) return 'bg-green-100 text-green-800 border-green-300';
    if (pos === 5 || pos === 6) return 'bg-blue-100 text-blue-800 border-blue-300';
    if (pos >= standings.length - 2) return 'bg-red-100 text-red-800 border-red-300';
    return 'bg-gray-100 text-gray-800 border-gray-300';
  };

  const calculateGoalDifference = (standing: Standing): number => {
    return parseInt(standing.overall_league_GF, 10) - parseInt(standing.overall_league_GA, 10);
  };

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Pos
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Team
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Played
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Won
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Draw
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Lost
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              GF
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              GA
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              GD
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Points
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {standings.map((standing, index) => {
            const goalDifference = calculateGoalDifference(standing);
            return (
              <tr
                key={`${standing.team_id}-${index}`}
                className="hover:bg-gray-50 transition-colors"
              >
                <td className="px-6 py-4 whitespace-nowrap">
                  <span
                    className={`inline-flex items-center justify-center h-8 w-8 rounded-full border-2 text-xs font-bold ${getPositionColor(
                      standing.overall_league_position
                    )}`}
                  >
                    {standing.overall_league_position}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="flex items-center">
                    <div className="flex-shrink-0 h-10 w-10">
                      {standing.team_badge ? (
                        <img
                          className="h-10 w-10 rounded-full object-cover"
                          src={standing.team_badge}
                          alt={standing.team_name}
                          onError={(e: React.SyntheticEvent<HTMLImageElement>) => {
                            e.currentTarget.style.display = 'none';
                          }}
                        />
                      ) : (
                        <div className="h-10 w-10 rounded-full bg-gray-200 flex items-center justify-center">
                          <span className="text-gray-500 text-xs font-bold">
                            {standing.team_name.charAt(0)}
                          </span>
                        </div>
                      )}
                    </div>
                    <div className="ml-4">
                      <div className="text-sm font-medium text-gray-900">
                        ({standing.team_id}) - {standing.team_name}
                      </div>
                      {standing.overall_promotion && (
                        <div className="text-xs text-gray-500 truncate max-w-xs">
                          {standing.overall_promotion}
                        </div>
                      )}
                    </div>
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-center text-sm text-gray-900">
                  {standing.overall_league_payed}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-center text-sm text-green-600 font-medium">
                  {standing.overall_league_W}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-center text-sm text-gray-600">
                  {standing.overall_league_D}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-center text-sm text-red-600 font-medium">
                  {standing.overall_league_L}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-center text-sm text-gray-900">
                  {standing.overall_league_GF}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-center text-sm text-gray-900">
                  {standing.overall_league_GA}
                </td>
                <td
                  className={`px-6 py-4 whitespace-nowrap text-center text-sm font-medium ${
                    goalDifference > 0
                      ? 'text-green-600'
                      : goalDifference < 0
                      ? 'text-red-600'
                      : 'text-gray-600'
                  }`}
                >
                  {goalDifference > 0 ? '+' : ''}
                  {goalDifference}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-center text-sm font-bold text-gray-900">
                  {standing.overall_league_PTS}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
      <div className="mt-4 px-6 py-3 bg-gray-50 border-t border-gray-200 text-xs text-gray-600">
        <div className="flex flex-wrap gap-4">
          <div className="flex items-center">
            <span className="inline-block h-3 w-3 rounded-full bg-green-500 mr-2"></span>
            <span>Top 4: Champions League</span>
          </div>
          <div className="flex items-center">
            <span className="inline-block h-3 w-3 rounded-full bg-blue-500 mr-2"></span>
            <span>5-6: Europa League</span>
          </div>
          <div className="flex items-center">
            <span className="inline-block h-3 w-3 rounded-full bg-red-500 mr-2"></span>
            <span>Bottom 2: Relegation Zone</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default StandingsTable;

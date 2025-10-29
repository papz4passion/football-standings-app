# Football Standings Frontend (TypeScript)

A React TypeScript frontend application for viewing football league standings, teams, and statistics.

## Features

- **TypeScript**: Fully typed React application for better developer experience
- **Cascading Dropdowns**: Select Country → League → Team
- **Dynamic Data**: All data fetched from backend API (no hardcoded data)
- **League Standings**: View complete standings with statistics
- **Team Filtering**: Filter standings by team name
- **Offline Mode**: Toggle offline mode for cached data
- **Cache Management**: Clear API cache
- **Responsive Design**: Mobile-friendly UI with Tailwind CSS
- **Loading States**: Visual feedback during API calls
- **Error Handling**: User-friendly error messages with retry
- **Type Safety**: Full TypeScript coverage for all components and services

## Tech Stack

- **React** 18.2.0
- **TypeScript** 5.3.2
- **Axios** 1.6.0 for API calls
- **Tailwind CSS** 3.3.0 for styling
- **React Scripts** 5.0.1 (Create React App)

## Prerequisites

- Node.js 16+ and npm
- Backend API running on `http://localhost:8080`

## Installation

```bash
# Install dependencies
npm install
```

## Running the Application

```bash
# Development server (http://localhost:3000)
npm start

# Production build
npm run build

# Run tests
npm test
```

## Project Structure

```
frontend/
├── public/
│   ├── index.html          # HTML template
│   └── manifest.json       # PWA manifest
├── src/
│   ├── components/
│   │   ├── Dropdown.tsx           # Reusable dropdown component
│   │   ├── StandingsTable.tsx     # Standings display table
│   │   ├── LoadingSpinner.tsx     # Loading indicator
│   │   └── ErrorMessage.tsx       # Error display component
│   ├── services/
│   │   └── footballService.ts     # API client
│   ├── types/
│   │   └── index.ts               # TypeScript type definitions
│   ├── App.tsx             # Main application component
│   ├── App.css             # App styles
│   ├── index.tsx           # React entry point
│   └── index.css           # Global styles with Tailwind
├── package.json            # Project configuration
├── tsconfig.json           # TypeScript configuration
├── tailwind.config.js      # Tailwind configuration
└── postcss.config.js       # PostCSS configuration
```

## TypeScript Types

All API responses and component props are fully typed:

- `Country`: Country data structure
- `League`: League data structure
- `Team`: Team data structure
- `Standing`: Standing data structure with all statistics
- `ApiError`: Error response structure
- Component prop types for all React components

## API Integration

The frontend integrates with the following backend endpoints:

- `GET /api/countries` - Get all countries
- `GET /api/leagues?country_id={id}` - Get leagues by country
- `GET /api/teams?league_id={id}` - Get teams by league
- `GET /api/standings?league_id={id}&team_name={name}` - Get standings
- `POST /api/offline?enabled={true|false}` - Toggle offline mode
- `DELETE /api/cache/clear` - Clear API cache
- `GET /actuator/health` - Health check

All API calls are proxied through `http://localhost:8080` as configured in `package.json`.

## Usage

1. **Select Country**: Choose from the country dropdown
2. **Select League**: Available leagues for the selected country appear
3. **View Standings**: League standings are displayed automatically
4. **Filter by Team** (Optional): Select a team to filter standings
5. **Toggle Offline Mode**: Enable/disable offline mode for cached responses
6. **Clear Cache**: Clear all cached API data

## Display Format

All data follows the format: `(ID) - Name`

- Country: `(country_id) - country_name`
- League: `(league_id) - league_name`
- Team: `(team_key) - team_name`

## Styling

The application uses Tailwind CSS with a custom blue theme:

- Primary color: Blue shades (50-900)
- Responsive design with mobile breakpoints
- Custom animations for loading states
- Color-coded standings (top 4, Europa, relegation)

## Environment Variables

Create a `.env` file for custom configuration:

```bash
REACT_APP_API_BASE_URL=http://localhost:8080
REACT_APP_API_TIMEOUT=30000
```

## Building for Production

```bash
# Create optimized production build
npm run build

# The build folder contains the production-ready files
# Deploy the contents of the build/ folder to your web server
```

## Docker Deployment

```bash
# Build Docker image
docker build -t football-standings-frontend .

# Run container
docker run -p 3000:80 football-standings-frontend
```

## Development

### Type Checking

```bash
# Run TypeScript compiler check
npx tsc --noEmit
```

### Adding New Types

Edit `src/types/index.ts` to add new TypeScript interfaces and types.

### API Service Extension

Edit `src/services/footballService.ts` to add new API methods. Ensure all methods have proper type annotations.

### Styling Customization

Edit `tailwind.config.js` to customize the theme.

## Troubleshooting

### API Connection Issues

- Ensure backend is running on `http://localhost:8080`
- Check CORS configuration in backend
- Verify proxy setting in `package.json`

### TypeScript Errors

```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

### Build Errors

```bash
# Clear build cache
rm -rf build
npm run build
```

## Type Safety Benefits

- **Compile-time error checking**: Catch errors before runtime
- **IntelliSense support**: Better autocomplete in IDEs
- **Refactoring confidence**: Safe refactoring with type checking
- **Self-documenting code**: Types serve as documentation
- **Better maintainability**: Easier to understand and modify code

## License

© 2025 Football Standings Application

import React from 'react';
import { DropdownProps } from '../types';

function Dropdown<T extends Record<string, any>>({
  label,
  options,
  value,
  onChange,
  disabled = false,
  loading = false,
  placeholder = 'Select an option',
  displayField,
  valueField,
  renderOption,
}: DropdownProps<T>): JSX.Element {
  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>): void => {
    onChange(event.target.value);
  };

  const getOptionLabel = (option: T): string => {
    if (renderOption) {
      return renderOption(option);
    }
    return `(${option[valueField]}) - ${option[displayField]}`;
  };

  return (
    <div className="flex flex-col space-y-2">
      <label
        htmlFor={`dropdown-${label}`}
        className="text-sm font-medium text-gray-700"
      >
        {label}
      </label>
      <select
        id={`dropdown-${label}`}
        value={value}
        onChange={handleChange}
        disabled={disabled || loading}
        className={`
          block w-full px-4 py-2.5 text-base
          border border-gray-300 rounded-lg
          bg-white text-gray-900
          focus:ring-2 focus:ring-primary-500 focus:border-primary-500
          transition-colors duration-200
          ${disabled || loading ? 'opacity-50 cursor-not-allowed bg-gray-50' : 'hover:border-gray-400 cursor-pointer'}
        `}
      >
        <option value="">
          {loading ? 'Loading...' : placeholder}
        </option>
        {options.map((option) => (
          <option
            key={String(option[valueField])}
            value={String(option[valueField])}
          >
            {getOptionLabel(option)}
          </option>
        ))}
      </select>
      {loading && (
        <div className="flex items-center text-sm text-gray-500">
          <svg
            className="animate-spin h-4 w-4 mr-2 text-primary-600"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle
              className="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              strokeWidth="4"
            />
            <path
              className="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
            />
          </svg>
          Loading options...
        </div>
      )}
    </div>
  );
}

export default Dropdown;

import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TooltipModule } from './tooltip-module/TooltipModule.js';
import { ToggleModule } from './toggle-module/ToggleModule.js'
import { EndScreenModule } from './endscreen-module/EndScreenModule.js';

// List of viewer modules that you want to use in your game
export const modules = [
	GraphicEntityModule,
	TooltipModule,
	ToggleModule,
	EndScreenModule
];

export const playerColors = [
  '#101010', // black
  '#f0f0f0' // white
];

export const options = [
    ToggleModule.defineToggle({
        toggle: 'sfenToggle',
        title: 'Show SFEN',
        values: {
            'ON': true,
            'OFF': false
        },
        default: false
    })
];

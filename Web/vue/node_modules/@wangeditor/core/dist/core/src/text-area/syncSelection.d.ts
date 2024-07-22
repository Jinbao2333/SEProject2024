/**
 * @description 同步 selection
 * @author wangfupeng
 */
import { IDomEditor } from '../editor/interface';
import TextArea from './TextArea';
/**
 * editor onchange 时，将 editor selection 同步给 DOM
 * @param textarea textarea
 * @param editor editor
 */
export declare function editorSelectionToDOM(textarea: TextArea, editor: IDomEditor): void;
/**
 * DOM selection change 时，把 DOM selection 同步给 slate
 * @param textarea textarea
 * @param editor editor
 */
export declare function DOMSelectionToEditor(textarea: TextArea, editor: IDomEditor): void;

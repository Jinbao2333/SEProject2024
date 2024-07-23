/**
 * @description render list elem
 * @author wangfupeng
 */
import { Element as SlateElement } from 'slate';
import { VNode } from 'snabbdom';
import { IDomEditor } from '@wangeditor/core';
declare const renderBulletedListConf: {
    type: string;
    renderElem: (elemNode: SlateElement, children: VNode[] | null, editor: IDomEditor) => VNode;
};
declare const renderNumberedListConf: {
    type: string;
    renderElem: (elemNode: SlateElement, children: VNode[] | null, editor: IDomEditor) => VNode;
};
declare const renderListItemConf: {
    type: string;
    renderElem: (elemNode: SlateElement, children: VNode[] | null, editor: IDomEditor) => VNode;
};
export { renderBulletedListConf, renderNumberedListConf, renderListItemConf };

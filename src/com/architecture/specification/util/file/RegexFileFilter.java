/*---------------------------------------------------------------------------*\
  $Id: 8acce293a477064167190e46813156d2006cfa29 $
  ---------------------------------------------------------------------------
  This software is released under a BSD-style license:

  Copyright (c) 2004-2007 Brian M. Clapper. All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are
  met:

  1.  Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

  2.  The end-user documentation included with the redistribution, if any,
      must include the following acknowlegement:

        "This product includes software developed by Brian M. Clapper
        (bmc@clapper.org, http://www.clapper.org/bmc/). That software is
        copyright (c) 2004-2007 Brian M. Clapper."

      Alternately, this acknowlegement may appear in the software itself,
      if wherever such third-party acknowlegements normally appear.

  3.  Neither the names "clapper.org", "clapper.org Java Utility Library",
      nor any of the names of the project contributors may be used to
      endorse or promote products derived from this software without prior
      written permission. For written permission, please contact
      bmc@clapper.org.

  4.  Products derived from this software may not be called "clapper.org
      Java Utility Library", nor may "clapper.org" appear in their names
      without prior written permission of Brian M. Clapper.

  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
  NO EVENT SHALL BRIAN M. CLAPPER BE LIABLE FOR ANY DIRECT, INDIRECT,
  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\*---------------------------------------------------------------------------*/

package com.architecture.specification.util.file;

import java.io.FileFilter;
import java.io.File;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <p><tt>RegexFileFilter</tt> implements a <tt>java.io.FileFilter</tt>
 * class that matches files using a regular expression. Multiple regular
 * expression filters can be combined using {@link AndFileFilter}
 * and/or {@link OrFileFilter} objects.</p>
 *
 * <p>A <tt>RegexFileFilter</tt> can be configured to operate on just the
 * simple file name, or on the file's path.</p>
 *
 * <p><tt>RegexFileFilter</tt> uses the <tt>java.util.regex</tt>
 * regular expression classes.</p>
 *
 * @see AndFileFilter
 * @see OrFileFilter
 * @see NotFileFilter
 * @see RegexFilenameFilter
 *
 * @version <tt>$Revision: 5812 $</tt>
 *
 * @author Copyright &copy; 2004-2007 Brian M. Clapper
 */
public class RegexFileFilter
    implements FileFilter
{
    /*----------------------------------------------------------------------*\
                            Private Data Items
    \*----------------------------------------------------------------------*/

    private FileFilterMatchType matchType = FileFilterMatchType.PATH;
    private Pattern pattern;

    /*----------------------------------------------------------------------*\
                            Constructor
    \*----------------------------------------------------------------------*/

    /**
     * Construct a new <tt>RegexFileFilter</tt> using the specified
     * pattern.
     *
     * @param regex     the regular expression to add
     * @param matchType <tt>FileFilterMatchType.FILENAME</tt> to match just the
     *                  filename, <tt>FileFilterMatchType.PATH</tt> to match
     *                  the path (via <tt>java.io.File.getPath()</tt>)
     *
     * @throws PatternSyntaxException  bad regular expression
     */
    public RegexFileFilter (String regex, FileFilterMatchType matchType)
        throws PatternSyntaxException
    {
        this.matchType = matchType;
        pattern = Pattern.compile (regex);
    }

    /**
     * Construct a new <tt>RegexFileFilter</tt> using the specified
     * pattern.
     *
     * @param regex      the regular expression to add
     * @param regexFlags regular expression compilation flags (e.g.,
     *                   <tt>Pattern.CASE_INSENSITIVE</tt>). See
     *                   the Javadocs for <tt>java.util.regex</tt> for
     *                   legal values.
     * @param matchType <tt>FileFilterMatchType.FILENAME</tt> to match just the
     *                  filename, <tt>FileFilterMatchType.PATH</tt> to match
     *                  the path (via <tt>java.io.File.getPath()</tt>)
     *
     * @throws PatternSyntaxException  bad regular expression
     */
    public RegexFileFilter (String              regex,
                            int                 regexFlags,
                            FileFilterMatchType matchType)
        throws PatternSyntaxException
    {
        this.matchType = matchType;
        pattern = Pattern.compile (regex, regexFlags);
    }

    /*----------------------------------------------------------------------*\
                              Public Methods
    \*----------------------------------------------------------------------*/

    /**
     * Determine whether a file is to be accepted or not, based on the
     * regular expressions in the <i>reject</i> and <i>accept</i> lists.
     *
     * @param file  The file to test. If the match type is
     *              <tt>FileFilterMatchType.FILENAME</tt>, then the value
     *              of <tt>file.getPath()</tt> is compared to the regular
     *              expression. If the match type is
     *              <tt>FileFilterMatchType.PATH</tt>, then the value of
     *              <tt>file.getName()</tt> is compared to the regular
     *              expression.
     *
     * @return <tt>true</tt> if the file matches, <tt>false</tt> if it doesn't
     */
    public boolean accept (File file)
    {
        String name = null;

        switch (matchType)
        {
            case PATH:
                name = file.getPath();
                break;

            case FILENAME:
                name = file.getName();
                break;

            default:
                assert (false);
        }

        return pattern.matcher (name).find();
    }
}
